package ch.hevs.Scanner;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.User.Client;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

// ClientHandler class
class UserHandler implements Runnable
{
    // A T T R I B U T S
    private DateFormat forDate = new SimpleDateFormat("yyyy/MM/dd");
    private DateFormat forTime = new SimpleDateFormat("hh:mm:ss");

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private BufferedReader buffin;
    private BufferedWriter buffout;

    private boolean isRunning;
    private Client client;
    private LinkedList<UserHandler> scannerUsersList;

    private ConsoleColors cc;


    // C O N S T R U C T E U R
    public UserHandler(Socket s, DataInputStream dis, DataOutputStream dos, LinkedList<UserHandler> connectedUsers)
    {
        this.socket = s;
        this.dis = dis;
        this.dos = dos;
        this.scannerUsersList = connectedUsers;

        //this.buffin = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        //this.buffout = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        this.isRunning = true;
        this.client = new Client();
        this.cc = ConsoleColors.PURPLE;
    }




    @Override
    public void run()
    {
        // 1) Récupérer les données du client (IP / Port / Contenu)
        deSerializeClientInformations();

        // 2) Dire au client, qu'on a bien reçu sa sérialisation, et qu'on est prêt à traiter les requêtes
        try
        {
            dos.writeUTF("Vous êtes connecté au scanner et à son user handler !" +
                    "\nVos informations ont été reçues et mis à disposition des autres utilisateurs connectés !" +
                    "\nVous pouvez désormais envoyer des requêtes au scanner.");
        } catch (IOException e)
        {
            System.err.println("SCANNER - run 1 : On n'a pas pu dire au client que l'on est prêt à traiter les requêtes !");
            throw new RuntimeException(e);
        }


        // 3) Handler en attente des requêtes du client
        String received;
        String sending;

        while (isRunning)
        {
            System.out.println( cc.PURPLE.getCOLOR() +
                                "SCANNER - Waiting for client requests...");
            try
            {
                // Se mettre en écoute des requetes du client
                // Get le message du client --> Serveur READ / Client WRITE
                received = dis.readUTF();

                switch (received)
                {
                    case "getList":
                        // Le client veut la liste des clients connectés
                        // Serialiser la liste, et l'envoyer au client
                        sendUsersList();
                        break;

                    case "sendClientInfos":
                        // Le client veut envoyer ses infos
                        // Dé-serialser les infos du client quand je les reçois --> READ
                        deSerializeClientInformations();
                        break;

                    case "stop":
                        // Le client se déconnecte
                        // je kill le socket connection--> close
                        // je kill le thread
                        System.out.println("Client " + this.socket + " sends exit...");
                        System.out.println("Closing this connection.");
                        isRunning = false;
                        this.socket.close();
                        System.out.println("Connection closed");
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e)
            {
                System.err.println("SCANNER - run 1 : Switch Case I/O Exception !");
                throw new RuntimeException(e);
            }


        }

        // 4) Fin du thread
        try
        {
            System.out.println( cc.PURPLE.getCOLOR() +
                    "SCANNER - Closing this connection.");
            this.socket.close();
        } catch (IOException e)
        {
            System.err.println("SCANNER - run 2 : On n'a pas pu fermer la connexion !");
            throw new RuntimeException(e);
        }

        System.out.println( cc.PURPLE.getCOLOR() +
                "SCANNER - End of thread.");
    }
    /**
     * 1) Récupérer les données du client (IP / Port / Contenu)
     * Stocker la première fois les informations du client
     * Stocker la dé-serialisation du client dans un objet Client
     */
    private void deSerializeClientInformations()
    {
        try
        {
            // 1) Récupérer les données du client (IP / Port / Contenu)
            // Se mettre en attente READ --> Le client quand il se connecte doit nous envoyer sa serialisation
            System.out.println("Waiting for client informations...");
            ObjectInputStream ois = new ObjectInputStream( socket.getInputStream() );
            this.client = (Client) ois.readObject();
            System.out.println( cc.PURPLE.getCOLOR() +
                    "Client informations received !" +
                    "\n Scanner Handler is ready to work !");

            System.out.println( cc.PURPLE.getCOLOR() +
                    "Client Infos : "+ client.toString());

        } catch (IOException e)
        {
            System.err.println("SCANNER - setClientInformations 1 : On n'a pas pu récupérer les données du client lors de la connexion !");
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("SCANNER - setClientInformations 2 : Dé-Serialisation impossible !");
            throw new RuntimeException(e);
        }
    }


    /**
     * Envoyer la liste des clients connectés du scanner au client qui a demandé la liste
     * @synchronized Tous les threads User Handler accèdent au même objet scannerUsersList
     * @throws IOException
     */
    public synchronized void sendUsersList()
    {
        // 1) Get la liste des clients connectés au scanner (scannerUsersList)
        // 2) Créer une liste de clients (clientsList) LinkedList<Client> à partir de chaque client des userHandlers de la liste scannerUsersList
        // 3) Serialiser la liste de clients (clientsList) & Envoyer la liste de clients au client via le socket connection --> WRITE
        // 4) Réponse du client --> READ

        // COTE CLIENT :
        // -) Le client reçoit la liste de clients, et la dé-serialise
        // -) Le client peut alors afficher la liste des clients connectés au scanner

        // 1)
        LinkedList<UserHandler> userListToSend = (LinkedList<UserHandler>) scannerUsersList.clone();

        // 2)
        LinkedList<Client> clientsList = new LinkedList<Client>();
        for (UserHandler uh : userListToSend)
        {
            clientsList.add( uh.getClient() );
        }

        // 3)
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(clientsList);

            // 4) Vérifier que le client a bien reçu la liste des clients connectés au scanner
            System.out.println("SCANNER - En attente de la réponse du client...");
            String confirmation = dis.readUTF();

            if (confirmation.equals("listReceived"))
            {
                System.out.println( cc.PURPLE.getCOLOR() +
                        "SCANNER - Client has received the list of connected users !");
            }
            else
            {
                System.out.println( cc.PURPLE.getCOLOR() +
                        "SCANNER - Client has NOT received the list of connected users !");
            }

        } catch (IOException e)
        {
            System.err.println("SCANNER - sendUsersList 1 : On n'a pas pu envoyer la liste des 'clients connectés au scanner' au client !");
            throw new RuntimeException(e);
        }
    }

    public Client getClient()
    {
        return client;
    }


}
