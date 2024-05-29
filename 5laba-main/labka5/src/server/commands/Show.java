package server.commands;

import server.managers.CollectionManager;

public class Show extends ServerCommand
{
    public Show()
    {
        super(null, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.show();
    }
}