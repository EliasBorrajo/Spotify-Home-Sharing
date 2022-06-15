package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.Logs.Log;

import java.util.ArrayList;


/*
 * Cette classe permet de lancer l'application. Elle permet de lancer le serveur et le client, pour un seul utilisateur.
 * -Role : Permet de lancer l'application, 1 thread client et 1 thread serveur par User.
 *         Contiendra la liste des musiques du User, son Port et son IP.
 * @author Elias Borrajo
 */
public class AppUser
{
    // A T T R I B U T S
    protected static Log log;
    private static boolean isRunningApp;
    private static ArrayList<Musique> musicList = new ArrayList<Musique>();
    private static final int PORT_DU_SERVEUR = 50000;
    private static String ip = "127.0.0.1";
    // On met par défaut l'IP à localHost, sera redéfini lors de la connexion avec le socket scanner,
    // pour avoir la bonne IP en fonction de la carte réseau utilisé.

    // M A I N
    /**
     * Lance l'application.
     * Va lancer le serveur et le client, chacun dans un thread.
     * @param args : inutiles ici.
     */
    public static void main(String[] args)
    {
        // Configuration des dossiers du USER
        Config.getConfig();
        log = new Log("userLog");

        // User aura 2 threads, un thread pour le client, l'autre pour le serveur
        Client client = new Client(ip,  PORT_DU_SERVEUR, musicList);
        Server server = new Server(     PORT_DU_SERVEUR, musicList);

        Thread clientThread = new Thread(client);
        Thread serverThread = new Thread(server);

        log.myLogger.info("USER : Démarrage du client & serveur");

        System.out.println("USER APPLICATION STARTED");
        System.out.println("Starting server thread");
        System.out.println("Starting client thread");


        serverThread.start();
        clientThread.start();
        isRunningApp = true;

        while (isRunningApp)
        {
            if (serverThread.isAlive() && clientThread.isAlive())
            {
                isRunningApp = true;
            }
            else
            {
                isRunningApp = false;
                log.myLogger.warning("One of the threads is dead, stopping the application");
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
                System.exit(0); // On ferme le programme (0 = succès).
            }
        }


    }
}
