package server.managers;

import server.commands.ServerCommand;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerCommandManager {
    private CollectionManager collectionManager;
    private int commandCounter = 0;
    private final Map<Integer, String> commandHistory = new LinkedHashMap<>();

    public ServerCommandManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    private void addToHistory(String commandName) {
        if (commandCounter >= 13) {
            Integer firstKey = commandHistory.keySet().iterator().next();
            commandHistory.remove(firstKey);
        }
        commandHistory.put(commandCounter++, commandName);
    }

    public String executeCommand(ServerCommand command) {
        this.addToHistory(command.getClass().getSimpleName());
        return command.execute(collectionManager);
    }

    public Map<Integer, String> getCommandHistory() {
        return commandHistory;
    }
}