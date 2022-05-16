package ch.hevs.Scanner;

import ch.hevs.User.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class Scanner {

    private static final int portDuServeur = 45000;
    private LinkedList <Client> listeConnectionUsers = new LinkedList <Client> ();

    public static void main(String[] args) throws IOException {


        try {

            // Création d'un serveur qui écoute sur le port 45000
            ServerSocket server = new ServerSocket(portDuServeur);

            // Confirmation console que le serveur est à l'écoute
            System.out.println("En attente de connections clients");

            // Création d'un point de communication "socket" pour chacun des clients qui se connectent
            Socket socket = server.accept();

            // Confirmation console qu'un client est connecté et affichage de ses informations
            System.out.println( "Un client est connecté");
            System.out.println( "IP et Port : " + socket.getRemoteSocketAddress().toString() );
            System.out.println( "Port du serveur : " + socket.getLocalPort() );

            Client client1 = new Client(socket.getInetAddress(), socket.getPort());

           client1.getListeDeMusiques();

            // Fermeture du serveur et de son socket d'accès
            server.close();
            socket.close();
        }
        // Dans le cas ou le serveur n'arrive pas à accepter les demandes d'accès clients
        catch (
                SocketException e)
        {
            e.printStackTrace();
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }

    }
}
