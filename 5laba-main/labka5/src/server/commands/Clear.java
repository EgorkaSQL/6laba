package server.commands;

import server.managers.CollectionManager;

public class Clear extends ServerCommand {

    public Clear() {
        super(null, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.clear();
    }
}