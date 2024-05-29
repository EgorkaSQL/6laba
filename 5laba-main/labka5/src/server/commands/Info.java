package server.commands;

import server.managers.CollectionManager;

public class Info extends ServerCommand {

    public Info() {
        super(null, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.info();
    }
}