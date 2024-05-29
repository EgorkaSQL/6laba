package server.commands;

import server.managers.CollectionManager;

import java.io.Serializable;

public abstract class ServerCommand implements Serializable {
    protected Long id;
    protected Serializable argument;

    protected ServerCommand(Long id, Serializable argument) {
        this.argument = argument;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Serializable getArgument() {
        return argument;
    }

    public abstract String execute(CollectionManager collectionManager);
}