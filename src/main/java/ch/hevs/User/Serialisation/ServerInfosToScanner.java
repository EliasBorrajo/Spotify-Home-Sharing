package ch.hevs.User.Serialisation;

import ch.hevs.User.Musique;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerInfosToScanner implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String ipUser;
    private String portUserServer;
    private ArrayList<Musique> musiques;

    public ServerInfosToScanner(String ipUser, String portUserServer, ArrayList<Musique> musiques)
    {
        this.ipUser = ipUser;
        this.portUserServer = portUserServer;
        this.musiques = musiques;
    }

    public String getIpUser()
    {
        return ipUser;
    }

    public String getPortUserServer()
    {
        return portUserServer;
    }

    public ArrayList<Musique> getMusiques()
    {
        return musiques;
    }
}
