package ch.hevs.Scanner;

import Logs.Formater;
import Logs.Log;
import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.User.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Elias
 * SCANNER --> 1 app scanner par subnet P2P
 * - Son adresse IP et PORT doivent être connus, les utilisateurs se partagent l'information oralement.
 * - Contient la liste des clients qui ont des fichiers audios
 * - adresse IP / Port / Liste fichiers
 */
public class AppScanner
{
    // A T T R I B U T S
    private static final int PORT_DU_SERVEUR = 45000;
    private static LinkedList<UserHandler> connectedUsers;
    private static boolean isRunning;
    private static ConsoleColors cc = ConsoleColors.PURPLE;

    private static ServerSocket server;
    private static Socket socket;
    /*****************************
     * LOG
     ****************************/
    static Log log = new Log();

    /**
     * Initiliser le Scanner avant de le lancer
     *
     * @throws IOException
     */
    private static void initScanner()
    {
        // Configuration des dossiers du USER
        Config.getConfig();

        isRunning = true;
        connectedUsers = new LinkedList<UserHandler>();

        // Création d'un serveur qui écoute sur le port 45000
        try
        {
            server = new ServerSocket(PORT_DU_SERVEUR);
        } catch (IOException e)
        {
            System.err.println("SCANNER : Impossible de créer un serveur sur le port " + PORT_DU_SERVEUR);
            throw new RuntimeException(e);
        }
    }

    // M A I N
    /**
     * Lance le scanner
     * Il sert aux clients qui se connectent au scanner, de savoir quels autres existent sur le subnet,
     * de pouvoir s'y connecter afin de leur récuperer leurs fichiers.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        // 1) Initialiser le Scanner en écoute

        // 2) Se mettre en attente & écoute, pour avoir des clients

        // 3) Quand un client se connecte, l'enregistrer dans une linked list, avec son IP / Port / Contenu

        // 4) Créer un thread pour chaque client

        // 5) Quand un client se déconnecte, l'enlever de la liste


        System.out.print(cc.getCOLOR());
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
            System.err.println("Scanner - Init OR Listen : Error");
            throw new RuntimeException(e);
        }
    }

    // M E T H O D E S
    /**
     *  Se mettre en attente & écoute, pour avoir des clients
     *  Quand un client se connecte, l'enregistrer dans une linked list, avec son IP et son contenu (liste des fichiers)
     * @author Elias
     */
    private static void listenAndAcceptUsers()
    {
        socket = null;

        try
        {
            // socket object to receive incoming client requests
            socket = server.accept();

            /****************************
             * LOGS
             ***************************/

            log.myLogger.info("Client " + socket.getInetAddress() + " is connected to the server on port " + socket.getPort());




            System.out.println("Un nouveau client s'est connecté : " + socket);
            // TODO : Créer un LOG des clients connectés : Client IP, Port, Liste de fichiers, date & heure de connection

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

        } catch (Exception e) {
            System.err.println( "SCANNER - ListenAndAccept : Erreur lors de la connexion d'un nouveau client");
            // Si le socket existe, essaye de le fermer
            try
            {
                socket.close();
            } catch (IOException ex)
            {
                System.err.println("Socket non fermé, impossible de le fermer car non existant");
                throw new RuntimeException(ex);
            }

            e.printStackTrace();
        }
    }

}
