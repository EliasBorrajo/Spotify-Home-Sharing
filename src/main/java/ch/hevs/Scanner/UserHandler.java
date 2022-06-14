package ch.hevs.Scanner;

import ch.hevs.User.Client;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import static ch.hevs.Scanner.AppScanner.log;

/**
 * @author Elias Borrajo
 * Classe crée par Scanner pour gérer les connexions des utilisateurs et l'interraction avec eux.
 * -Rôle : Gérer les requetes des utilisateurs.
 *
 * Création de 1 socket pour chaque utilisateur connecté, et de 1 UserHandler pour chaque utilisateur connecté.
 */
class UserHandler implements Runnable
{
    // A T T R I B U T S
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    private boolean isRunning;
    private Client client;
    private LinkedList<UserHandler> scannerUsersList;



    // C O N S T R U C T E U R
    public UserHandler(Socket s, DataInputStream dis, DataOutputStream dos, LinkedList<UserHandler> connectedUsers) throws IOException
    {
        this.socket = s;
        this.dis = dis;
        this.dos = dos;
        this.scannerUsersList = connectedUsers;

        this.isRunning = true;
        this.client = null;
    }


    /**
     * Gérer les requetes des utilisateurs.
     * String received : Requete de l'utilisateur à traiter.
     */
    @Override
    public void run()
    {
        String received;

        // 1) Récupérer les données du client (IP / Port / Contenu). Stocker la première fois les informations du client.
        try
        {
            received = dis.readUTF();
            //System.out.println("Initialisation of the client : " + received);
            deSerializeClientInformations();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }



        // 2) Handler en attente des requêtes du client
        while (isRunning)
        {
            System.out.println("SCANNER - Waiting for client requests...");

            try
            {
                // Se mettre en écoute des requetes du client
                // Get le message du client --> Serveur READ / Client WRITE
                received = dis.readUTF();

                switch (received)
                {

                    case "getClientsList":
                        // Le client veut la liste des clients connectés
                        // Serialiser la liste, et l'envoyer au client
                        System.out.println("Client wants the client list");
                        sendUsersList();
                        break;

                    case "sendClientInfos":
                        // Le client veut envoyer ses infos
                        // Dé-serialser les infos du client quand je les reçois --> READ
                        System.out.println("Client wants to send his infos");
                        deSerializeClientInformations();
                        break;

                    case "logout":
                        // Le client se déconnecte
                        // je kill le socket connection--> close
                        // je kill le thread
                        System.out.println("Client wants to stop");
                        System.out.println("Client " + this.socket + " sends exit...");
                        System.out.println("Closing this connection.");
                        isRunning = false;
                        removeUserFromList();
                        log.myLogger.info("Client " + socket.getInetAddress() + " disconnected");
                        this.socket.close();
                        System.out.println("Connection closed");
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            }
            catch (IOException e)
            {
                log.myLogger.severe("Client " + client.getUserIp() + " forced to logout : " + e);
                System.out.println("Client forced to logout");
                isRunning = false;
                removeUserFromList();
                System.err.println("SCANNER - run 1 : Switch Case has a I/O Exception !");

            }

        }

        // 3) Fin du thread
        try
        {
            System.out.println("SCANNER - Closing this connection.");
            this.socket.close();
        }
        catch (IOException e)
        {
            System.err.println("SCANNER - run 2 : On n'a pas pu fermer la connexion !");
            throw new RuntimeException(e);
        }

        System.out.println("SCANNER - End of thread.");
    }

    /**
     * Supprimer l'utilisateur de la liste des utilisateurs connectés.
     * @synchronized : Car la liste est dans le Scanner, et que elle est partagé par plusieurs thread.
     * On copie la liste ici en temporaire, pour pouvoir traiter la liste et savoir quel User supprimer de la liste du Scanner.
     */
    private synchronized void removeUserFromList()
    {
        // La liste est synchronised --> En faire une copie,
        // trouver ou se trouve le user à supprimer, puis le supprimer dans la vraie liste
        LinkedList<UserHandler> tempList = (LinkedList<UserHandler>) scannerUsersList.clone();
        //int locationInList = -1;
        for (int i = 0; i < tempList.size(); i++)
        {
            // Trouverl'index de l'IP du user que je possède dans la liste temp
            if (this.client.getUserIp().equals(tempList.get(i).client.getUserIp()))
            {
                scannerUsersList.remove(i);
                System.out.println("User " + this.client.getUserIp() + " removed from the list");
                break;
            }
        }
    }


    /**
     * Récupérer les données du client (IP / Port / Contenu)
     * Stocker la dé-serialisation du client dans un objet Client
     */
    private void deSerializeClientInformations()
    {
        ObjectInputStream ois = null;
        try
        {
            // 1) Récupérer les données du client (IP / Port / Contenu)
            // Se mettre en attente READ --> Le client quand il se connecte doit nous envoyer sa serialisation
            System.out.println("Waiting for client informations...");

            // Réinitialiser le client s'il existe déjà
            this.client = null;

            ois = new ObjectInputStream(socket.getInputStream());
            this.client = (Client) ois.readObject();

            System.out.println("Client informations received !");
            log.myLogger.info("Client Infos : " + client.toString());

            String sendConfirmation = "Client_Received";
            dos.writeUTF(sendConfirmation);
            dos.flush();

        }
        catch (IOException e)
        {
            String msg ="SCANNER - setClientInformations 1 : On n'a pas pu récupérer les données du client lors de la connexion !";
            log.myLogger.severe(msg + e.toString());
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            String msg = "SCANNER - setClientInformations 2 : Dé-Serialisation impossible !";
            log.myLogger.severe(msg + e.toString());
            throw new RuntimeException(e);
        }
    }


    /**
     * Envoyer la liste des clients connectés du scanner au client qui a demandé la liste
     *
     * @throws IOException : Si le client n'a pas pu récupérer la liste du scanner
     * @synchronized Tous les threads User Handler accèdent au même objet scannerUsersList
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

        // 1) Get la liste des clients connectés au scanner (scannerUsersList)
        LinkedList<UserHandler> userListToSend = (LinkedList<UserHandler>) scannerUsersList.clone();

        // 2)  Créer une liste de clients (clientsList) LinkedList<Client>
        // à partir de chaque client des userHandlers de la liste scannerUsersList
        LinkedList<Client> clientsList = new LinkedList<Client>();
        for (UserHandler uh : userListToSend)
        {
            clientsList.add(uh.getClient());
        }

        // 3) Serialiser la liste de clients (clientsList)
        // & Envoyer la liste de clients au client via le socket connection --> WRITE
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(clientsList);
            oos.flush();


            // 4) Vérifier que le client a bien reçu la liste des clients connectés au scanner
            System.out.println("SCANNER - En attente de la réponse du client...");
            String confirmation = dis.readUTF();

            if (confirmation.equals("listReceived"))
            {
                System.out.println("SCANNER - Client has received the list of connected users !");
            }
            else
            {
                System.out.println("SCANNER - Client has NOT received the list of connected users !");
            }

        }
        catch (IOException e)
        {
            String msg = "SCANNER - sendUsersList 1 : On n'a pas pu envoyer la liste des 'clients connectés au scanner' au client !";
            log.myLogger.severe(msg + e.toString());
            throw new RuntimeException(e);
        }
    }

    // G E T T E R S
    public Client getClient()
    {
        return client;
    }


}
