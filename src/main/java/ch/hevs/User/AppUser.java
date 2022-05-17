package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.User.Client;
import ch.hevs.User.Server;

/**
 * Cette classe permet de lancer l'application. Elle permet de lancer le serveur et le client, pour un seul utilisateur.
 *
 * @author Elias Borrajo
 */
public class AppUser
{
    public static void main(String[] args)
    {
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

        Config.getConfig();

    }
}
