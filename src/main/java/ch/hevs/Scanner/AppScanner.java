package ch.hevs.Scanner;

import ch.hevs.Logs.Log;
import ch.hevs.Configurations.Config;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


/**
 * @author Elias Borrajo & Arthur Avez
 * SCANNER --> 1 app scanner par subnet P2P
 * - Rôle : Serveur à l'écoute des connexions des utilisateurs, et transmet la gestion de l'utilisateur à UserHandler.
 *          Création d'un socket pour chaque utilisateur connecté.
 *
 * - Son adresse IP et PORT doivent être connus, les utilisateurs se partagent l'information oralement.
 * - Le scanner est un serveur qui attend les connexions des utilisateurs.
 * - Le scanner envoie les données à l'utilisateur qui l'a demandé.
 * - Le scanner sert de serveur de synchronisation pour les utilisateurs.
 * - Contient la liste des clients avec leurs fichiers audios mis à disposition sur leur Server.
 *      - Client =  adresse IP / Port / Liste fichiers audios
 */
public class AppScanner
{
    // A T T R I B U T S
    private static final int PORT_DU_SERVEUR = 45000;
    private static LinkedList<UserHandler> connectedUsers;
    private static boolean isRunning;

    private static ServerSocket server;
    private static Socket socket;
    protected static Log log;

    /**
     * Initiliser le Scanner avant de le lancer
     * @throws IOException : Si on n'arrive pas à créer un socket d'écoute sur le port du scanner.
     */
    private static void initScanner()
    {
        // Configuration des dossiers du USER
        Config.getConfig();

        isRunning = true;
        connectedUsers = new LinkedList<UserHandler>();
        log = new Log("scannerLog");

        // Création d'un serveur qui écoute sur le port 45000
        try
        {
            server = new ServerSocket(PORT_DU_SERVEUR);
        } catch (IOException e)
        {
            String msg = "SCANNER : Unable to create a server on the portt " + PORT_DU_SERVEUR;
            log.myLogger.severe(msg + " : " + e.toString());
            System.exit(1); // Arrêt du programme en cas d'erreur
            throw new RuntimeException(e);

        }
    }

    // M A I N
    /**
     * Lance le scanner
     * Il sert aux clients qui se connectent au scanner, de savoir quels autres existent sur le subnet,
     * de pouvoir s'y connecter afin de leur récupérer leurs fichiers.
     * @param args : Ne sert à rien ici, pas traité
     */
    public static void main(String[] args)
    {
        // 1) Initialiser le Scanner en écoute

        // 2) Se mettre en attente & écoute, pour avoir des clients

        // 3) Quand un client se connecte, l'enregistrer dans une linked list, avec son IP / Port / Contenu

        // 4) Créer un thread pour chaque client

        // 5) Quand un client se déconnecte, l'enlever de la liste


        System.out.println("Scanner started");
        try
        {
            //1) Initialiser le Scanner en écoute
            initScanner();

            //2) Se mettre en attente & écoute, pour avoir des clients
            while (isRunning)
            {
                listenAndAcceptUsers();
            }

            // Fermeture du serveur et de son socket d'accès
            System.out.println("Scanner Closed");
            server.close();
            socket.close();


        } catch (IOException e)
        {
            String msg = "Scanner - Init OR Listen : Error";
            log.myLogger.warning(msg + " : " + e.toString());
            throw new RuntimeException(e);
        }

        System.exit(0); // Arrêt du programme
    }

    // M E T H O D E S
    /**
     *  Se mettre en attente & écoute, pour avoir des clients
     *  Quand un client se connecte, l'enregistrer dans une list, avec son IP, Port et son contenu (liste des fichiers)
     * @author Elias
     */
    private static void listenAndAcceptUsers()
    {
        socket = null;

        try
        {
            // socket object to receive incoming client requests
            socket = server.accept();
            String msg = "Client ip " + socket.getInetAddress() + " is connected to the Scanner with : " + socket + " local IP : " + socket.getLocalAddress();
            log.myLogger.info(msg);
            System.out.println(msg);

            //System.out.println("Un nouveau client s'est connecté : " + socket);

            // obtaining input and out streams
            DataInputStream  dis = new DataInputStream ( socket.getInputStream()  );
            DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
            System.out.println("Assigning new thread for this client");

            // create a new thread object
            UserHandler uh = new UserHandler(socket, dis, dos, connectedUsers);
            Thread t = new Thread( uh );

            // Ajouter le user au liste des clients connectés
            connectedUsers.add(uh);

            // Invoking the start() method
            t.start();

        }
        catch (Exception e)
        {
            String msg = "SCANNER - ListenAndAccept : Error when connecting a new client";
            log.myLogger.severe(msg + " : " + e.toString());
            System.err.println(msg);
            // Si le socket existe, essaye de le fermer
            try
            {
                socket.close();
            } catch (IOException ex)
            {
                log.myLogger.severe("Impossible to close socket connection properly: " + ex.toString());
                System.err.println("Impossible to close socket connection properly, because it doesn't exist.");
                throw new RuntimeException(ex);
            }

            e.printStackTrace();
        }
    }

}
