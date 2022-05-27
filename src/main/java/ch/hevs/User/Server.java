package ch.hevs.User;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server implements Runnable
{
    // A T T R I B U T S
    private static int PORT_DU_SERVEUR;
    private ArrayList<Musique> listeDeMusiques;
    private static ServerSocket listeningSocket;
    private static Socket socket;
    private boolean isRunning;
    private ConsoleColors cc;


    // C O N S T R U C T E U R
    public Server(int portDuServeur, ArrayList<Musique> musicList)
    {
        cc = ConsoleColors.YELLOW;
        listeDeMusiques = musicList;
        this.PORT_DU_SERVEUR = portDuServeur;

        try
        {
            listeningSocket = new ServerSocket(PORT_DU_SERVEUR);
        } catch (IOException e)
        {
            System.err.println("SERVER : Impossible de créer un serveur sur le port " + portDuServeur);
            throw new RuntimeException(e);
        }
    }

    // R U N N A B L E
    @Override
    public void run()
    {
        System.out.println(cc.YELLOW.getCOLOR()+ "Server is running");

        startApp();
    }



    // M E T H O D E S
    private void startApp()
    {
        // 1) Initialiser le Serveur en écoute

        // 2) Se mettre en attente & écoute, pour avoir des clients

        // 3) Créer un thread pour chaque client

        System.out.print(cc.getCOLOR());
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

    private void listenAndAcceptUsers()
    {
        socket = null;

        try
        {
            // socket object to receive incoming client requests
            socket = listeningSocket.accept();

            System.out.println("Un nouveau client s'est connecté : " + socket);
            // TODO : Créer un LOG des clients connectés : Client IP, Port, Liste de fichiers, date & heure de connection

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
