package server.commands;

import server.managers.CollectionManager;

public class Save extends ServerCommand
{
    public Save() {
        super(null, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        collectionManager.save();
        return "Коллекция успешно сохранена в файл " + collectionManager.getFilePath();
    }
}