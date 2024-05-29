package server.commands;

import server.managers.CollectionManager;

public class RemoveKey extends ServerCommand
{
    public RemoveKey(Long id)
    {
        super(id, null);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.removeKey(getId());
    }
}
