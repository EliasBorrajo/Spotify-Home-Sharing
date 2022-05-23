package ch.hevs.User;

import ch.hevs.Scanner.AppScanner;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

// La classe implémente l'extension Runnable et Serializable
public class Client implements Runnable, Serializable
{
    // A T T R I B U T S
    // Le mot clé transient permet de choisir les attributs de la classe que l'on ne veut pas sérialiser
    private static final long serialVersionUID = 44L;
    private String userIp;
    private int serverPort;
    private ArrayList<Musique> musicList;
    private transient Socket socket;

    private transient boolean isConnected;
    private transient boolean isRunning;
    private transient ConsoleColors consoleColors;

    // C O N S T R U C T E U R
    public Client(String userIP, int serverPort, ArrayList<Musique> musicList)
    {
        this.userIp = userIP;
        this.serverPort = serverPort;
        this.musicList = musicList;

        isConnected = false;
        isRunning = true;
        consoleColors = ConsoleColors.GREEN;
    }

    // R U N N A B L E
    /**
     * Cette méthode permet de lancer la connection d'un client à un serveur
     */
    @Override
    public void run()
    {
        // Tentative de connection au serveur...
            do
            {
                // On récupère les informations nécessaires àa la connection
                String []informations = getConnectionInformations();
                String serverIp = informations[0];
                int serverPort = Integer.parseInt(informations[1]);
            // Tentative de connection au serveur
            try
            {
                connectToScanner(serverIp, serverPort);

            } catch (Exception e)
            {
                // Message d'erreur en cas d'échec de connection
                System.err.println("La connection a échoué. Veuillez vérifier les informations de connection et essayer à nouveau.");
            }
            // ... tant que la connection n'a pas été établie
        } while (isConnected == false);

        showMenuToClient();
    }

    // M E T H O D E S
    /**
     * Cette méthode permet de lancer la connection d'un client à un serveur
     * @return un tableau de String contenant l'adresse IP du serveur ainsi que son port
     */
    public String [] getConnectionInformations ()
    {
        // Création d'un tableau permettant de retourner toutes les infos nmécessaires à la connection à un serveur
        String [] informations = new String[2];
        // A gérer plus tard car esthétique pure...
        System.out.print(consoleColors.GREEN.getCOLOR());
        // Information console
        System.out.println("Client is running :");
        System.out.println("Enter your IP address : ");
        // Création d'un scanner d'écoute des saisis console
        Scanner sc = new Scanner(System.in);
        // Création d'une variable String qui récupérera la prochaine saisie client dans la console
        String serverIp = sc.nextLine();
        // Mettre serverIp dans le tableau de retour
        informations[0] = serverIp;
        System.out.println("Enter your port : ");
        // Création d'une variable int qui récupérera la prochaine saisie client dans la console
        int serverPort = sc.nextInt();
        // Mettre serverPort dans le tableau de retour
        informations[1] = Integer.toString(serverPort);
        // Retour du tableau avec les infos remplies
        return informations;
    }

    /**
     * Cette méthode permet de se connecter au scanner
     * @param serverIp : l'adresse IP du serveur
     * @param serverPort : le port du serveur
     */
    public void connectToScanner(String serverIp, int serverPort) {
        try {
            // Tentative de connection au serveur par le biais d'un socket
            socket = new Socket(serverIp, serverPort);
            // Confirmation de connection
            System.out.println("I'm connected !");
            isConnected = true;
        }
        // En cas d'échec de connection au scanner
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet d'afficher les différentes fonctionnalités de l'application au client
     */
   public void showMenuToClient()
    {

            // Variable permettant d'enregistrer le choix console du client
            int userChoice;
            // Scanner qui permettra d'écouter les saisies client dans la console
            Scanner scan = new Scanner(System.in);
            // Do...
            do {
                // Présentation du menu de fonctionnalités au client
                System.out.println("Menu : ");
                System.out.println("1) Show the available servers with their musics to stream");
                System.out.println("2) Play a music");
                System.out.println("X) Update my music list");
                System.out.println("3) Logout");
                // Tant que la saisie dans la console client est différente d'un int
                while (!scan.hasNextInt()) // If not an int !
                {
                    // Message d'erreur
                    System.err.println("Please enter a valid data format");
                    // Ecoute de la saisie console
                    scan.next();
                }
                // On enregistre la saisie du client dans une variable
                userChoice = scan.nextInt();
                // En fonction de la saisie du client, on appelle la méthode qui est concernée
                switch (userChoice) {
                    // Option permettant de montrer au client les différents serveurs connectés au scanner
                    case 1:
                        System.out.println("Loading available servers...");
                        showServersFromScanner();
                        break;
                    // Option permettant au client de jouer une musique
                    case 2:
                        System.out.println("Loading your music...");
                        sendSerializedClientPackage();
                        // playSelectedMusic();
                        break;
                    // Option permettant au client de se déconnecter de l'application
                    case 3:
                        System.out.println("Logout...");
                        logout();
                        break;
                    // Dans le cas ou le client saisirait une option inexistante
                    default:
                        // Message d'erreur
                        System.err.println("Invalid option, please refer to the displayed menu");
                        break;
                }
                // Tant que le client ne se déconnecte pas
            } while (isRunning == true);
        }

    // TODO : à terminer...
    private void showServersFromScanner()
    {
        // Création d'une liste permettant de stocker la liste en provenance du Clientandler
        LinkedList <Client> serversList = null;

        // T E S T
        ArrayList <Musique> clientList = null;
        Client c1 = new Client("127.0.0.1", 99000, clientList);
        Client c2 = new Client("127.0.0.2", 57000, clientList);
        Client c3 = new Client("127.0.0.3", 23000, clientList);

        serversList.add(c1);
        serversList.add(c2);
        serversList.add(c3);
        
        // 1) Appeler la méthode  getListOfServersFromScanner
        serversList = getListOfServersFromScanner();

        // 2) Appeler la méthode deserializeAvailableServersList();
        deserializeAvailableServersList();

        // 3) Afficher la liste de serveurs disponibles
        int cpt1 = 0;
        for (Client client: serversList)
        {
            System.out.println("Client : " + cpt1 + " IP : " + client.userIp + " Port : " + client.serverPort);
            cpt1++;
            int cpt2 = 0;
            for (Musique music : client.musicList)
            {
                System.out.println("\t Musique n° " + cpt2 + " : " + music.getMusicFileName());
                cpt2++;
            }
        }
    }

    // TODO : à terminer...
    private LinkedList<Client> getListOfServersFromScanner() {
       // Envoyer un message par le socket "getClientList"
        return null;
    }

    // TODO : à terminer...
    private void playSelectedMusic(String ip, int serverPort, String musicName)
    {

    }

    /**
     * Cette méthode permet de se déconnecter du programme
     */
    private void logout() {
        // Scanner d'écoute des saisies console
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure ? (true/false)");
        // Tant que la saisie du client n'est pas booléenne
        while (!sc.hasNextBoolean()) {
            System.out.println("Are you sure ? (true/false)");
            // Ecoute de la prochaine saisie client
            sc.next();
        }
        // On inverse la valeur du booléen afin de pouvoir quitter la boucle du menu
        isRunning = !sc.nextBoolean();

        System.out.println("Goodbye");
    }

    /**
     * Cette méthode permet d'envoyer un objet client sérialisé au scanner
     */
    public void sendSerializedClientPackage() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // Tentative d'envoie de l'objet client au scanner
            oos.writeObject(this);
            oos.close();
        // En cas d'envoi impossible
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode permet de désérialiser la liste de serveurs reçu afin d'en afficher son contenu au client
     * @return une liste de clients (serveurs)
     */
    public LinkedList <Client> deserializeAvailableServersList() {
        // Liste permettant de stocker la liste de serveurs reçue du ClientHandler
        LinkedList <Client> serversList = null;
        try {
            // En attente de réception de la liste de serveurs
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // Tentative de désérialisation de l'objet list une fois reçu
            serversList = (LinkedList<Client>) ois.readObject();
            ois.close();
        // En cas de réception impossible
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // On renvoi la liste de serveurs reçue
        return serversList;
    }

    @Override
    public String toString() {
        return "Client{" +
                "userIp='" + userIp + '\'' +
                ", serverPort=" + serverPort +
                ", musicList=" + musicList +
                '}';
    }
}