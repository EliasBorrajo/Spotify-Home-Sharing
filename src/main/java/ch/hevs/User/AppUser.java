package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Cette classe permet de lancer l'application. Elle permet de lancer le serveur et le client, pour un seul utilisateur.
 *
 * @author Elias Borrajo
 */
public class AppUser
{
    private static boolean isRunningApp;

    /**
     * Lance l'application.
     * Va lancer le serveur et le client, chacun dans un thread.
     * @param args
     */
    public static void main(String[] args)
    {
        // Configuration des dossiers du USER
        Config.getConfig();

        // User aura 2 threads, un thread pour le client, l'autre pour le serveur
        Client client = new Client();
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



/*
        isRunningApp = true;

        while (isRunningApp)
        {

        }

        // On attend que les threads soient terminés pour fermer le programme
        System.out.println("Waiting for threads to finish");
        System.out.println("Closing server thread");
        System.out.println("Closing client thread");
        serverThread.interrupt();
        clientThread.interrupt();

        System.out.println("Closing application");
        System.exit(0);

*/

    }
}
