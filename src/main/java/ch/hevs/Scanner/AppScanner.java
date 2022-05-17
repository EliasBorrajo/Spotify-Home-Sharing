package ch.hevs.Scanner;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.User.Client;
import ch.hevs.User.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * @author Elias & Arthur
 * SCANNER --> 1 app scanner par subnet P2P
 *      - Son adresse IP et PORT doivent être connus, les utilisateurs se partagent l'information oralement.
 *      - Contient la liste des clients qui ont des fichiers audios
 *          - adresse IP / Port / Liste fichiers
 */

    /*
 		- HasTable <IP, Content> AllUsersContent
		- List AllUsersIPConected

		○ ScanSubnet()
		○ ScanUsersContent(Users[].contentList)
		○ ShowLogs()

* */
public class AppScanner
{
    private static final int PORT_DU_SERVEUR = 45000;
    private static LinkedList <Client> connectedUsers; // Liste des clients connectés
    private static boolean isRunning;
    private static ConsoleColors cc = ConsoleColors.PUPLE;;


    private static ServerSocket server;
    private static Socket socket;


    public static void main(String[] args)
    {
        // 1) Initialiser le Scanner en écoute

        // 2) Se mettre en attente & écoute, pour avoir des clients

        // 3) Quand un client se connecte, l'enregistrer dans une linked list, avec son IP et son contenu

        // 4) Quand un client rajoute un fichier, mettre à jour la liste des fichiers du client concerné
            // Le user server fait un refresh de son contenu, qui va update le contenu sur scanner.
            // Le user client cherche es fichiers, demandera un refresh au scanner pour avoir les derniers updates.

        // 5) Quand un client se déconnecte, l'enlever de la liste


        System.out.print(cc.getCOLOR());
        System.out.println("Scanner started");
        try
        {
            //1) Initialiser le Scanner en écoute
            initScanner();

            //2) Se mettre en attente & écoute, pour avoir des clients
            while(isRunning)
            {
                ScanSubnet();
            }

            // Fermeture du serveur et de son socket d'accès
            System.out.println("Scanner stopped");
            server.close();
            socket.close();



        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void initScanner() throws IOException
    {
        isRunning = true;
        connectedUsers = new LinkedList<Client>();

        // Création d'un serveur qui écoute sur le port 45000
        server = new ServerSocket(PORT_DU_SERVEUR);

    }

    private static void ScanSubnet() throws IOException
    {
        // Création d'un point de communication "socket" pour chacun des clients qui se connectent
        socket = server.accept();

        // Confirmation console qu'un client est connecté et affichage de ses informations
        System.out.println( "Un nouveau client s'est connecté");
        System.out.println( "IP et Port : " + socket.getRemoteSocketAddress().toString() );
        System.out.println( "Port du serveur : " + socket.getLocalPort() );

        //Client client1 = new Client(socket.getInetAddress(), socket.getPort());
        //client1.getListeDeMusiques();

        // TODO : Créer un LOG des clients connectés : Client IP, Port, Liste de fichiers, date & heure de connection


        // 3) Get le client serialisé et l'ajouter à la liste des clients connectés

        // 4) Quand un client rajoute un fichier, mettre à jour la liste des fichiers du client concerné
            // Le user server fait un refresh de son contenu, qui va update le contenu sur scanner.
            // Le user client cherche es fichiers, demandera un refresh au scanner pour avoir les derniers updates.


    }

    private static void UpdateUsersContent()
    {
    }

    private static void ShowLogs()
    {
    }

    private static void ScanUsersContent()
    {
    }




}
