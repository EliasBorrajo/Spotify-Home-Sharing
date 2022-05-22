package ch.hevs.Scanner;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.User.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

// ClientHandler class
class UserHandler implements Runnable
{
    private DateFormat forDate = new SimpleDateFormat("yyyy/MM/dd");
    private DateFormat forTime = new SimpleDateFormat("hh:mm:ss");

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    private boolean isRunning;
    private Client client;
    private LinkedList<UserHandler> scannerUsersList;

    private ConsoleColors cc;


    // Constructor
    public UserHandler(Socket s, DataInputStream dis, DataOutputStream dos, LinkedList<UserHandler> connectedUsers)
    {
        this.socket = s;
        this.dis = dis;
        this.dos = dos;

        this.isRunning = true;
        this.client = new Client();
        this.scannerUsersList = connectedUsers;
        this.cc = ConsoleColors.PURPLE;
    }


    @Override
    public void run()
    {
        // 1) Récupérer les données du client (IP / Port / Contenu)
        // TODO : Stocker la première fois les informations du client
        // TODO : Se mettre en read --> Le client quand il se connecte doit nous envoyer sa serialisation
        // TODO : Stocker la serialisation du client dans un objet Client

        // Comme ça, les autres handlers peuvent : getList<Userhandler> list.get(i).getClient().getIP()


        // 2) Handler en attente des requêtes du client

        String received;
        String toreturn;

        while (isRunning)
        {
            try
            {
                // Se mettre en écoute des requetes du client
                // Get le message du client --> Serveur READ / Client WRITE
                received = dis.readUTF();


                switch(received)
                {
                    case "getList":
                        // Le client veut la liste des clients connectés
                        // Serialiser la liste, et l'envoyer au client
                        break;

                    case "sendClientInfos":
                        // Le client veut envoyer ses infos
                        // Dé-serialser les infos du client quand je les reçois --> READ
                        break;

                    case "stop":
                        // Le client se déconnecte
                        // je kill le socket connection--> close
                        // je kill le thread
                        isRunning = false;
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }



                // TODO : DATA INPUT STREAM AND OUTPUT STREAM - FAIRE COMME EN COURS AVEC BUFFERS
                /**
                 * On se met TOUJOUR en ecoute du client, et en fonction de ce que il nous envoie, switch case de son choix
                 */

                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time]..\n" +
                                 "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();

                if (received.equals("Exit"))
                {
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                switch (received)
                {

                    case "Date":
                        toreturn = forDate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time":
                        toreturn = forTime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public synchronized void sendUsersList()
    {
        // TODO : Serialiser la liste des clients connectés
        // TODO : Envoyer la liste au client
        LinkedList<UserHandler> userListToSend = (LinkedList<UserHandler>) scannerUsersList.clone();

        // itérer à travers userListToSend, pour y recuperer les infos de chacun,
        // créer une LISTE DE CLIENTS, et serialiser liste de clients


    }





}
