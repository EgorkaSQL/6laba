package server.commands;

import server.managers.CollectionManager;

public class Help extends ServerCommand {

    public Help() {
        super(null, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.help();
    }
}
