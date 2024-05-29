package server.commands;

import client.generatorss.OrganizationGeneratorr;
import client.mycollection.Organization;
import server.managers.CollectionManager;
import server.managers.XmlManager;

import java.io.Serializable;

public class Insert extends ServerCommand
{
    public Insert(Long id, Organization organization) {
        super(id, organization);
    }

    @Override
    public String execute(CollectionManager collectionManager) {
        Organization organization = (Organization) getArgument();
        return collectionManager.insert(organization);
    }
}