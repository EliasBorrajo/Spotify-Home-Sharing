package ch.hevs.User;


import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.util.ArrayList;

/*
 * Cette classe permet de lancer l'application. Elle permet de lancer le serveur et le client, pour un seul utilisateur.
 * @author Elias Borrajo
 */

public class AppUser
{
    private static boolean isRunningApp;
    private static ArrayList<Musique> musicList = new ArrayList<Musique>();

    /**
     * Lance l'application.
     * Va lancer le serveur et le client, chacun dans un thread.
     */

    public static void main(String[] args)
    {
        // Configuration des dossiers du USER
        Config.getConfig();

        // User aura 2 threads, un thread pour le client, l'autre pour le serveur
        Client client = new Client("127.0.0.1", 50000, musicList );
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

