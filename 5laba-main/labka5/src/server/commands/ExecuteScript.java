package server.commands;

import client.generatorss.OrganizationGeneratorr;
import client.mycollection.Organization;
import client.mycollection.OrganizationType;
import server.managers.CollectionManager;
import server.managers.ServerCommandManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class ExecuteScript extends ServerCommand {
    private ServerCommandManager commandManager;
    private HashSet<File> scriptsBeingExecuted = new HashSet<>();
    private OrganizationGeneratorr organizationBuilder;

    public ExecuteScript(ServerCommandManager commandManager, OrganizationGeneratorr organizationBuilder) {
        super(null, null);
        this.commandManager = commandManager;
        this.organizationBuilder = organizationBuilder;
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        if (getArgument() == null) {
            return "Ошибка: команда execute_script должна иметь аргумент (имя скриптового файла).";
        }

        File scriptFile = new File((String) getArgument());
        if (!scriptFile.exists()) {
            return "Ошибка: файл скрипта не существует!";
        }

        if (!scriptsBeingExecuted.add(scriptFile)) {
            return "Ошибка: обнаружена попытка рекурсивного вызова execute_script!";
        }

        try (Scanner fileScanner = new Scanner(scriptFile)) {
            while (fileScanner.hasNextLine()) {
                String fileCommand = fileScanner.nextLine().trim();
                String[] fileCommandParts = fileCommand.split(" ", 2);
                String fileCommandName = fileCommandParts[0];
                String fileCommandArgs = fileCommandParts.length > 1 ? fileCommandParts[1] : null;

                switch (fileCommandName) {
                    case "insert":
                        Organization org = organizationBuilder.createOrganizationFromUserInput();
                        commandManager.executeCommand(new Insert(Long.parseLong(fileCommandArgs), org));
                        break;
                    case "update":
                        commandManager.executeCommand(new Update(Long.parseLong(fileCommandArgs), null));
                        break;
                    case "remove_key":
                        commandManager.executeCommand(new RemoveKey(Long.parseLong(fileCommandArgs)));
                        break;
                    case "show":
                        commandManager.executeCommand(new Show());
                        break;
                    case "replace_if_greater":
                        commandManager.executeCommand(new ReplaceIfGreater(Long.parseLong(fileCommandArgs), null));
                        break;
                    case "remove_greater":
                        commandManager.executeCommand(new RemoveGreater(Long.parseLong(fileCommandArgs)));
                        break;
                    case "remove_all_by_type":
                        commandManager.executeCommand(new RemoveAllByType(OrganizationType.valueOf(fileCommandArgs)));
                        break;
                    case "info":
                        commandManager.executeCommand(new Info());
                        break;
                    case "filter_by_annual_turnover":
                        commandManager.executeCommand(new FilterByAnnualTurnover(Integer.parseInt(fileCommandArgs)));
                        break;
                    case "exit":
                        commandManager.executeCommand(new Exit());
                        System.out.println("Завершение работы клиента...");
                        System.exit(0);
                    case "count_less_than_annual_turnover":
                        commandManager.executeCommand(new CountLessThanAnnualTurnover(Integer.parseInt(fileCommandArgs)));
                        break;
                    case "clear":
                        commandManager.executeCommand(new Clear());
                        break;
                    case "execute_script":
                        commandManager.executeCommand(new ExecuteScript(commandManager, organizationBuilder));
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестная команда: " + fileCommandName);
                }
            }
        } catch (FileNotFoundException e) {
            return "Произошла ошибка при чтении файла " + getArgument();
        } finally {
            scriptsBeingExecuted.remove(scriptFile);
        }

        return "Скрипт " + getArgument() + " успешно выполнен.";
    }
}