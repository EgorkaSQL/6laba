package server.managers;

import client.mycollection.Organization;
import client.mycollection.OrganizationType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager
{
    private Hashtable<Long, Organization> organizations;
    private LocalDate creationDate;
    private String filePath;

    public CollectionManager(Hashtable<Long, Organization> organizations,  String filePath)
    {
        this.organizations = organizations;
        this.creationDate = LocalDate.now();
        this.filePath = filePath;
        loadData(filePath);
    }

    private void loadData(String fileName)
    {
        XmlManager xmlManager = new XmlManager();
        organizations = xmlManager.readFromFile(fileName);
        System.out.println("Коллекция загружена из файла " + fileName);
    }

    public String getFilePath()
    {
        return filePath;
    }

    public boolean containsId(long id)
    {
        return organizations.containsKey(id);
    }

    public String help()
    {
        return ("Введите одну из следующих команд:\n" +
                "help : вывести справку по доступным командам\n" +
                "info: вывести в стандартный поток вывода информацию о коллекции\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "insert null {element} : добавить новый элемент с заданным ключом\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_key null : удалить элемент из коллекции по его ключу\n" +
                "clear : очистить коллекцию\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "history : вывести последние 13 команд (без их аргументов)\n" +
                "replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого\n" +
                "remove_all_by_type type : удалить из коллекции все элементы, значение поля type которого эквивалентно заданному\n" +
                "count_less_than_annual_turnover annualTurnover : вывести количество элементов, значение поля annualTurnover которых меньше заданного\n" +
                "filter_by_annual_turnover annualTurnover : вывести элементы, значение поля annualTurnover которых равно заданному");
    }

    public Hashtable<Long, Organization> getOrganizations()
    {
        return organizations;
    }

    public void save() {
        XmlManager xmlManager = new XmlManager();
        try {
            xmlManager.writeToFile(organizations, filePath);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при попытке сохранить коллекцию в файл " + filePath);
            e.printStackTrace();
        }
    }

    public String info()
    {
        return ("Тип коллекции: " + organizations.getClass().getName() + "\nДата инициализации: " + creationDate + "\nКоличество элементов: " + organizations.size());
    }

    public String show() {
        if (organizations.isEmpty()) {
            return "Коллекция пуста.";
        } else {
            return organizations.values().stream()
                    .sorted(Comparator.comparing(Organization::getName))
                    .map(Organization::toString)
                    .collect(Collectors.joining("\n"));
        }
    }

    public String insert(Organization organization) {
        long id = 1;
        while (organizations.containsKey(id)) {
            id += 1;
        }
        organization.setId(id);
        organizations.put(id, organization);
        return("Добавлен элемент с ID " + id);
    }

    public String update(Long id, Organization organization) {
        if (organizations.containsKey(id)) {
            organization.setId(id);
            organizations.put(id, organization);
            return "Элемент с ID " + id + " был обновлен.";
        } else {
            return "Элемент с ID " + id + " не существует. Обновление невозможно.";
        }
    }

    public String removeKey(Long id)
    {
        if (organizations.containsKey(id))
        {
            organizations.remove(id);
            return ("Элемент с ID " + id + " удален.");
        }
        else
        {
            return ("Элемент с ID " + id + " не найден. Удаление невозможно.");
        }
    }

    public String clear()
    {
        organizations.clear();
        return ("Коллекция была очищена.");
    }

    public String removeAllByType(OrganizationType type)
    {
        long initialSize = organizations.size();
        organizations.values().removeIf(org -> org.getType().equals(type));
        long newSize = organizations.size();

        if (initialSize > newSize)
        {
            return ("Были удалены элементы. Размер коллекции уменьшился на " + (initialSize - newSize));
        }
        else
        {
            return ("Удаление элементов не произошло. Размер коллекции не изменился.");
        }
    }

    public int countLessThanAnnualTurnover(int annualTurnover)
    {
        return (int) organizations.values().stream()
                .filter(org -> org.getAnnualTurnover() < annualTurnover)
                .count();
    }

    public String filterByAnnualTurnover(int annualTurnover)
    {
        return organizations.values().stream()
                .filter(org -> org.getAnnualTurnover() == annualTurnover)
                .map(Organization::toString)
                .collect(Collectors.joining("\n"));
    }
}
