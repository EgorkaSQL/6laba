package server.managers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import client.mycollection.Organization;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

public class XmlManager
{
    public Hashtable<Long, Organization> readFromFile(String fileName)
    {
        XStream xStream = new XStream();
        xStream.addPermission(NoTypePermission.NONE);

        xStream.allowTypeHierarchy(client.mycollection.Organization.class);
        xStream.allowTypeHierarchy(java.lang.Long.class);
        xStream.allowTypeHierarchy(java.util.Hashtable.class);

        xStream.alias("Organization", client.mycollection.Organization.class);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            return (Hashtable<Long, Organization>) xStream.fromXML(reader);
        }
        catch (IOException e)
        {
            System.err.println("Произошла ошибка при чтении файла " + fileName + ": " + e.getMessage());
        }

        return new Hashtable<>();
    }

    public void writeToFile(Hashtable<Long, Organization> organizations, String fileName) throws IOException
    {
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.alias("Organization", client.mycollection.Organization.class);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))
        {
            writer.write(xStream.toXML(organizations));
        }
    }
}