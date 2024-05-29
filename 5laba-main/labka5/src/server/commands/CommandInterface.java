package server.commands;

import java.io.Serializable;

public interface CommandInterface extends Serializable
{
    String execute(ServerCommand command);
}