package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Scanner;

import client.mycollection.Organization;
import server.commands.Save;
import server.commands.ServerCommand;
import server.managers.CollectionManager;
import server.managers.ServerCommandManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.XmlManager;

public class ServerApp
{
    private static final int BUFFER_SIZE = 65535;

    private static Hashtable<Long, Organization> organizations;
    public static ServerCommandManager commandManager;
    private static final Logger LOGGER = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) throws IOException
    {
        LOGGER.info("Начало работы сервера...");
        organizations = new Hashtable<>();
        String filePath = "organizations.xml";

        CollectionManager collectionManager = new CollectionManager(organizations, filePath);
        commandManager = new ServerCommandManager(collectionManager);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Сохранение состояния коллекции перед выходом...");
            collectionManager.save();
        }));

        try
        {
            int port = 1234;
            DatagramChannel channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            LOGGER.info("Сервер запущен и прослушивает порт {}", port);

            while (true) {
                if (System.in.available() > 0) {
                    String serverCommand = new Scanner(System.in).nextLine().trim();

                    if (serverCommand.equalsIgnoreCase("save")) {
                        Save save = new Save();
                        System.out.println(save.execute(collectionManager));
                    }
                }
                buffer.clear();
                SocketAddress clientAddress = channel.receive(buffer);

                if (clientAddress != null) {
                    LOGGER.info("Получен новый запрос от клиента: {}", clientAddress);
                    buffer.flip();

                    try {
                        ServerCommand command = deserializeCommand(buffer.array());
                        String result = commandManager.executeCommand(command);

                        buffer.clear();
                        buffer.put(result.getBytes(StandardCharsets.UTF_8));
                        buffer.flip();

                        channel.send(buffer, clientAddress);
                        LOGGER.info("Отправлен ответ клиенту: {}", clientAddress);
                    } catch (IOException | ClassNotFoundException e) {
                        LOGGER.error("Ошибка при десериализации команды: " + e.getMessage());
                    }
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Ошибка при создании серверного сокета: " + e.getMessage());
        }
    }

    private static ServerCommand deserializeCommand(byte[] commandBytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(commandBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (ServerCommand) objectInputStream.readObject();
        }
    }
}