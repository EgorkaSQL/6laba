package server.commands;

import client.mycollection.OrganizationType;
import server.managers.CollectionManager;

public class RemoveAllByType extends ServerCommand {
    public RemoveAllByType(OrganizationType type) {
        super(null, type);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        OrganizationType type = (OrganizationType) getArgument();
        return collectionManager.removeAllByType(type);
    }
}
