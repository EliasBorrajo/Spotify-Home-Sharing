package ch.hevs.User;


import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/*
 * Cette classe permet de lancer l'application. Elle permet de lancer le serveur et le client, pour un seul utilisateur.
 * @author Elias Borrajo
 */

public class AppUser
{
    private static boolean isRunningApp;
    private static ArrayList<Musique> musicList = new ArrayList<Musique>();
    private static final int PORT_DU_SERVEUR = 50000;
    //private static final int USER_IP;


    /**
     * Lance l'application.
     * Va lancer le serveur et le client, chacun dans un thread.
     * @param args
     */
    public static void main(String[] args)
    {
        // Configuration des dossiers du USER
        Config.getConfig();

        //USER_IP = //TODO : Faire INET address vu au debout des cours, come ça on a notre propre IP !
        // Le user doit choisir entre ses 2 aresses IP wn WLAN ou LAN

        try
        {
            Enumeration<NetworkInterface> allni = NetworkInterface.getNetworkInterfaces();

        } catch (SocketException e)
        {
            throw new RuntimeException(e);
        }

        // User aura 2 threads, un thread pour le client, l'autre pour le serveur
        Client client = new Client("127.0.0.1", PORT_DU_SERVEUR, musicList );
        Server server = new Server();

        Thread clientThread = new Thread(client);
        Thread serverThread = new Thread(server);

        ConsoleColors cc = ConsoleColors.BLUE;

        System.out.print(cc.BLUE.getCOLOR());
        System.out.println("USER APPLICATION STARTED");
        System.out.println("Starting server thread");
        System.out.println("Starting client thread");
        System.out.print(cc.ANSI_RESET.getCOLOR());

        serverThread.start();
        clientThread.start();

        if (serverThread.isAlive() && clientThread.isAlive())
        {
            isRunningApp = true;
            //client.;
            //sleep(1000);

        }
        else
        {
            isRunningApp = false;
        }

        if (isRunningApp == false)
        {
            // On attend que les threads soient terminés pour fermer le programme
            System.out.println("Waiting for threads to finish");
            System.out.println("Closing server thread");
            System.out.println("Closing client thread");
            serverThread.interrupt();
            clientThread.interrupt();

            System.out.println("Closing application");
            System.exit(0);
        }

    }
}
