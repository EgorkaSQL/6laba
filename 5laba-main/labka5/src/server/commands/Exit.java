package server.commands;

import server.managers.CollectionManager;

public class Exit extends ServerCommand {

    public Exit() {
        super(null, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        return "Завершение работы!";
    }
}