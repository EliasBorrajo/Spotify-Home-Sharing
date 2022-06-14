package ch.hevs.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static ch.hevs.User.AppUser.log;


/**
 * Le serveur est un thread qui attend les clients qui se connectent.
 * Il crée un thread pour chaque client qui se connecte.
 * Role : Serveur à l'écoute des clients, et les redirige vers un ClientHandler qui gère les requêtes du client.
 */
public class Server implements Runnable
{
    // A T T R I B U T S
    private static int PORT_DU_SERVEUR;
    private ArrayList<Musique> listeDeMusiques;
    private static ServerSocket listeningSocket;
    private static Socket socket;
    private boolean isRunning;


    // C O N S T R U C T E U R

    /**
     * Constructeur du serveur.
     * @param portDuServeur : le port du serveur défini dans AppUser
     * @param musicList : la liste de musiques définie dans AppUser
     */
    public Server(int portDuServeur, ArrayList<Musique> musicList)
    {
        this.listeDeMusiques = musicList;
        this.PORT_DU_SERVEUR = portDuServeur;
        this.isRunning = true;

        try
        {
            listeningSocket = new ServerSocket(PORT_DU_SERVEUR);
        } catch (IOException e)
        {
            String msg = "SERVER : Impossible de créer un serveur sur le port " + portDuServeur;
            System.err.println(msg);
            throw new RuntimeException(e);
        }
    }

    // R U N N A B L E

    /**
     * Méthode run du serveur.
     */
    @Override
    public void run()
    {
        System.out.println("Server is running");

        startApp();
    }



    // M E T H O D E S
    /**
     * Méthode qui démarre l'application.
     */
    private void startApp()
    {
        // 1) Initialiser le Serveur en écoute

        // 2) Se mettre en attente & écoute, pour avoir des clients

        // 3) Créer un thread pour chaque client

        System.out.println("Server started");
        try
        {
            //1) Se mettre en attente & écoute, pour avoir des clients
            while (isRunning)
            {
                listenAndAcceptUsers();
            }

            // Fermeture du serveur et de son socket d'accès
            System.out.println("Server Closed");
            listeningSocket.close();
            socket.close();


        } catch (IOException e)
        {
            System.err.println("SERVEUR - Init OR Listen : Error");
            throw new RuntimeException(e);
        }

    }

    /**
     * Méthode qui écoute les clients et les redirige vers un ClientHandler.
     */
    private void listenAndAcceptUsers()
    {
        socket = null;

        try
        {
            // socket object to receive incoming client requests
            socket = listeningSocket.accept();

            log.myLogger.info("Client " + socket.getInetAddress() + " is connected to the server on port " + socket);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream ( socket.getInputStream()  );
            DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
            System.out.println("Assigning new thread for this client");

            // create a new thread object
            ClientHandler ch = new ClientHandler(socket, dis, dos, listeDeMusiques);
            Thread t = new Thread( ch );

            // Invoking the start() method
            t.start();

        }
        catch (Exception e)
        {
            System.err.println( "SERVEUR - ListenAndAccept : Erreur lors de la connexion d'un nouveau client");
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
