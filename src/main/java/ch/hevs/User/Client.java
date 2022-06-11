package ch.hevs.User;


import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.ToolBox.SleepTimerConverter.Converter;
import javafx.scene.media.MediaPlayer;

import javax.sound.midi.Soundbank;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

// La classe implémente l'extension Runnable et Serializable
public class Client implements Runnable, Serializable
{
    // A T T R I B U T S
    private static final long serialVersionUID = 44L;
    private String userIp;
    private int serverPort;
    private ArrayList<Musique> musicList;

    // Le mot clé transient permet de choisir les attributs de la classe que l'on ne veut pas sérialiser
    private transient Socket socket;
    private transient DataInputStream dis;
    private transient DataOutputStream dos;
    private transient boolean isConnected;
    private transient boolean isRunning;
    private transient ConsoleColors consoleColors;

    // Attributs pour la connection à un autre user
    private transient Socket p2pSocket;
    private transient DataInputStream p2pDis;
    private transient DataOutputStream p2pDos;


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
        // 1) On scanne les musiques présentes sur notre PC pour remplire la liste de musiques du client (musicList)
        updateMyMusicList();

        // 2) Tentative de connection au serveur...
        do
        {
            // On récupère les informations nécessaires àa la connection
            String[] informations = getConnectionInformations();
            String scannerIP = informations[0];
            int scannerPort = Integer.parseInt(informations[1]);

            // Tentative de connection au serveur
            try
            {
                connectToScanner(scannerIP, scannerPort); // TODO : Cette ligne à decommenter pour la version finale
                //connectToScanner("127.0.0.1", 45000);
            }
            catch (Exception e)
            {
                System.err.println("La connection a échoué. Veuillez vérifier les informations de connection et essayer à nouveau.");
            }

            // ... tant que la connection n'a pas été établie
        } while (isConnected == false);

        // 3) On Serialise le client et on l'envoie au scanner
        try
        {
            dos.writeUTF("ClientReady");
            dos.flush();
            sendSerializedClientPackage("...");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }


        // 4) On affiche le menu du client, et on choisit un ordre à envoyé au scanner
        if (isConnected)
        {
            showMenuToClient();
        }

    }

    // M E T H O D E S
    /**
     * Cette méthode permet de lancer la connection d'un client à un serveur
     *
     * @return un tableau de String contenant l'adresse IP du serveur ainsi que son port
     */
    public String[] getConnectionInformations()
    {
        // Création d'un tableau permettant de retourner toutes les infos nécessaires à la connection à un serveur
        String[] informations = new String[2];

        System.out.print(consoleColors.GREEN.getCOLOR());
        System.out.println("Client is running...");
        System.out.println("Connect to the scanner with the following informations :");
        System.out.println("Enter scanner IP address : ");

        // Création d'une variable String qui récupérera la prochaine saisie client dans la console
        Scanner sc = new Scanner(System.in);
        String serverIp = sc.nextLine();
        informations[0] = serverIp;

        // Création d'une variable int qui récupérera la prochaine saisie client dans la console
        System.out.println("Enter scanner port : ");
        int serverPort = sc.nextInt();
        informations[1] = Integer.toString(serverPort);

        // Retour du tableau avec les infos remplies
        return informations;
    }

    /**
     * Cette méthode permet de se connecter au scanner
     *
     * @param serverIp   : l'adresse IP du serveur
     * @param serverPort : le port du serveur
     */
    public void connectToScanner(String serverIp, int serverPort)
    {
        try
        {
            // Tentative de connection au serveur par le biais d'un socket
            socket = new Socket(serverIp, serverPort);
            // Confirmation de connection
            System.out.println("I'm connected !");
            isConnected = true;

            // Création des flux de données pour la communication avec le serveur (InputStream et OutputStream)
            dis = new DataInputStream (socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }
        // En cas d'échec de connection au scanner
        catch (IOException e)
        {
            System.err.println("Connection to scanner failed !");
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet d'afficher les différentes fonctionnalités de l'application au client
     */
    public void showMenuToClient()
    {
        int userChoice;
        Scanner scan = new Scanner(System.in);

        try
        {
            do
            {
                // Présentation du menu de fonctionnalités au client
                System.out.println("MENU : ");
                System.out.println("1) Show the available servers with their musics to stream");
                System.out.println("2) Play a music");
                System.out.println("3) Update my music list available on my PC to the scanner");
                System.out.println("4) Logout");

                // Tant que la saisie dans la console client est différente d'un int
                while (!scan.hasNextInt()) // If not an int !
                {
                    // Message d'erreur
                    System.err.println("Please enter a valid input number ! Try again...");
                    // Ecoute de la prochaine saisie console
                    scan.next();
                }
                userChoice = scan.nextInt();
                String requestToSend;

                switch (userChoice)
                {
                    // Option permettant de montrer au client les différents serveurs connectés au scanner
                    case 1:
                        System.out.println("Loading available servers...");
                        requestToSend = "getClientsList";
                        dos.writeUTF(requestToSend);
                        dos.flush();
                        showServersFromScanner(requestToSend);
                        break;

                    // Option permettant au client de jouer une musique
                    case 2:
                        // 1) on se connecte au serveur d'un client
                        // 2) On envoie au serveur la commande de lecture d'une musique
                        // 3) on crée un nouveau thread MAIN pour lire la musique
                        System.out.println("Connecting to another user...");
                        if (connectToServer())
                        {
                            requestToSend = "playMusic";
                            selectSongToPlay(requestToSend);
                        }
                        else
                        {
                            System.err.println("Connection to user failed !");
                        }
                        break;

                    // Option permettant au client de MAJ sa liste de musiques disponibles sur son PC
                    case 3:
                        requestToSend = "sendClientInfos";
                        System.out.println("Loading your music...");
                        updateMyMusicList();
                        System.out.println("Your music list has been updated !");

                        dos.writeUTF(requestToSend);
                        dos.flush();
                        sendSerializedClientPackage(requestToSend);
                        System.out.println("Your music list has been sent to the scanner !");
                        break;

                    // Option permettant au client de se déconnecter de l'application
                    case 4:
                        System.out.println("Logout...");
                        requestToSend = "logout";
                        if (!logout())
                        {
                            dos.writeUTF(requestToSend);
                            dos.flush();
                        }

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
        catch (IOException e)
        {
            System.err.println("Error while sending request WRITE UTF to scanner");
            throw new RuntimeException(e);
        }

        System.out.println("Goodbye");
        try
        {
            // Fermeture des flux de données
            dis.close();
            dos.close();
            socket.close();
            isConnected = false;

        }
        catch (IOException e)
        {
            System.err.println("CLIENT - RUN 1 : Could not close the socket before leaving and ending this thread.");
            throw new RuntimeException(e);
        }
        finally
        {
            System.exit(0); // On quitte l'application CLIENT sans erreur
        }

    }

    private void selectSongToPlay(String requestToSend)
    {

        try
        {
            // 1) Envoyer la requete au serveur --> Lancer une musique
            p2pDos.writeUTF(requestToSend);
            p2pDos.flush();
            boolean isPlaying = true;
            boolean isSongFound = false;

            do
            {
                // 2) donner la musique à jouer au serveur
                System.out.println("Selectionnez le titre de la musique à jouer : ");
                System.out.println("Pour quitter vers le menu, tapez 'EXIT'");
                Scanner scan = new Scanner(System.in);
                String songToPlay = scan.nextLine();

                if (songToPlay.equals("EXIT"))
                {
                    System.out.println("Vous allez vous déconnecter du serveur, retour au menu principal...");
                    isSongFound = false;
                    isPlaying = false;
                    p2pSocket.close();
                    return; // On quitte la fonction
                }

                System.out.println("TEST : " + songToPlay);
                // 3) Envoyer le nom de la musique au serveur,
                // attendre la réponse pour vérifier que il ait bien encore la musique disponible
                p2pDos.writeUTF(songToPlay);
                p2pDos.flush();

                // 4) Si la musique est disponible, lancer le thread de lecture de la musique
                String serverMusicSearchResponse = p2pDis.readUTF();
                if (serverMusicSearchResponse.equals("SongFound"))
                {
                    System.out.println("Musique trouvée, lancement du thread de lecture...");
                    isSongFound = true;
                }
                else // 5) Sinon, afficher un message d'erreur
                {
                    System.out.println("La musique n'est pas disponible, veuillez en choisir une autre...");
                    isSongFound = false;
                }

            }while (isSongFound == false);

            // 6) Si la musique est disponible, lancer le thread de lecture de la musique

            // Envoyer la connection au AudioPlayer dans son thread pour reçevoir la musique en streaming et la jouer
            // On commande le thread depuis ici.
            // Ici on envoie les commandes au serveur, le serveur envoie les informations à AudioPlayer
            InputStream is = new BufferedInputStream( p2pSocket.getInputStream() );
            AudioPlayer audioPlayer = new AudioPlayer(is);

            Thread musicThread = new Thread(audioPlayer);
            musicThread.start();
            audioPlayer.play();

            // 7) Boucle de commande de lecture de la musique PLAY PAUSE STOP

            do
            {
                System.out.println("Que voulez-vous faire ? (PLAY, PAUSE, STOP)");
                Scanner scan = new Scanner(System.in);
                String command = scan.nextLine();

                switch (command)
                {
                    case "PLAY":
                        System.out.println("Playing...");
                        p2pDos.writeUTF(command);
                        p2pDos.flush();
                        audioPlayer.play();
                        break;

                    case "PAUSE":
                        System.out.println("Paused...");
                        p2pDos.writeUTF(command);
                        p2pDos.flush();
                        audioPlayer.pause();
                        break;

                    case "STOP":
                        System.out.println("Stopped...");
                        p2pDos.writeUTF(command);
                        p2pDos.flush();
                        audioPlayer.pause();
                        isPlaying = false;
                        break;

                    default:
                        System.out.println("Invalid command, please try again...");
                        break;
                }

            } while (isPlaying == true);

            // 8) Quitter le thread de lecture de la musique
            System.out.println("End of playing a music ! back to menu...");
            //p2pSocket.close(); // TODO ???

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (UnsupportedAudioFileException e)
        {
            System.out.println("Unsupported audio file");
            System.out.println("Audio Player could not play the song");
            throw new RuntimeException(e);
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException(e);
        }


    }

    private boolean connectToServer()
    {
        boolean isConnected = false;
        System.out.println("Connexion au serveur d'un autre user, entrez ses informations : ");
        System.out.println("Entrez l'adresse IP du Client  : ");
        Scanner scan = new Scanner(System.in);
        String serverIp = scan.nextLine();

        System.out.println("Entrez le port du Client  : ");
        int serverPort = scan.nextInt();

        try
        {
            p2pSocket = new Socket(serverIp, serverPort);
            System.out.println("Connexion au serveur du User Réussie !");
            p2pDis = new DataInputStream(p2pSocket.getInputStream());
            p2pDos = new DataOutputStream(p2pSocket.getOutputStream());

            isConnected = true;
            return isConnected;
        }
        catch (UnknownHostException e)
        {
            System.err.println("Aucun serveur trouvé avec ces informations, retour au menu, veuillez réessayer");
            //throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            System.err.println("Impossible de se connecter au serveur, veuillez réessayer");
            //throw new RuntimeException(e);
        }
        finally
        {
            /*try
            {
                p2pSocket.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }*/

            System.out.println("TEST 3");
            // Si on a pas réussi à se connecter au serveur, on skip le RETURN TRUE, et on envoie le false
            //return isConnected;

        }

        return isConnected;
    }

    /**
     * Cette méthode permet d'envoyer un objet client sérialisé au scanner
     */
    public void sendSerializedClientPackage(String requestToSend)
    {
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            // Tentative d'envoie de l'objet client au scanner
            oos.writeObject(this);
            oos.flush();

            System.out.println("Client sent to scanner !");

            // Attend une confirmation du scanner
            String receivedMessage = dis.readUTF();
            System.out.println("Received message from scanner : " + receivedMessage);
            if (receivedMessage.equals("Client_Received"))
            {
                System.out.println("Scanner got my Client information Serialization !");
            }
            else
            {
                System.err.println("Scanner did not received my Client information Serialization !");
                System.err.println("Something went wrong !, disconnecting from scanner, try again...");
                socket.close();
                isConnected = false;
                isRunning = false;
            }

        }
        catch (IOException e)
        {
            System.err.println("CLIENT - SendSerializedClientPackage : Could not send the object to the Scanner.");
            e.printStackTrace();

            System.out.println("Something went wrong !, disconnecting from scanner, try again...");
            try
            {
                socket.close();
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            isConnected = false;
            isRunning = false;
        }
    }


    /**
     * Cette méthode permet de montrer au client les différents serveurs connectés au scanner
     */
    private void showServersFromScanner(String requestToSend)
    {
        // 1) Recuperer la liste des clients connectés au scanner
        LinkedList<Client> usersConnectedToScanner = getListOfServersFromScanner(requestToSend);

        // 3) Afficher la liste de serveurs disponibles
        System.out.println("-------------------------------------------------------");
        int cpt_Users = 0;
        for (Client client : usersConnectedToScanner)
        {
            System.out.println("Client : " + cpt_Users + " IP : " + client.userIp + " Port : " + client.serverPort);
            cpt_Users++;

            int cpt_Musiques = 0;
            for (Musique music : client.musicList)
            {
                System.out.println("\t Musique n° " + cpt_Musiques + " : " + music.getMusicFileName());
                cpt_Musiques++;
            }
            System.out.println("-------------------------------------------------------");
        }
    }

    /**
     * Cette methode permet de récuperer la liste des users connectés au scanner
     *
     * @return : la liste des clients connectés au scanner
     */
    private LinkedList<Client> getListOfServersFromScanner(String requestToSend)
    {
        LinkedList<Client> usersConnectedToScanner = null;

        try
        {
            //dos.writeUTF(requestToSend);
            //dos.flush();

            System.out.println("Waiting for clients list...");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            usersConnectedToScanner = (LinkedList<Client>) ois.readObject();


            System.out.println("Received clients list !");
            // Envoyer la onfirmation de reception de la liste
            dos.writeUTF("listReceived");
            dos.flush();

            return usersConnectedToScanner;

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }

    }

    // TODO : à terminer...
    private void playSelectedMusic(String ip, int serverPort, String musicName)
    {

    }

    /**
     * Cette méthode permet de se déconnecter du programme
     */
    private boolean logout()
    {
        // Scanner d'écoute des saisies console
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure ? (true/false)");

        // Tant que la saisie du client n'est pas booléenne
        while (!sc.hasNextBoolean())
        {
            System.out.println("Are you sure ? (true/false)");
            // Ecoute de la prochaine saisie client
            sc.next();
        }
        // On inverse la valeur du booléen afin de pouvoir quitter la boucle du menu principal
        return isRunning = !sc.nextBoolean();
    }

    /**
     * Cette methode GET, va aller voir le répertoire UPLOAD de mon PC
     * et va mettre à jour ma liste de musique à distribuer.
     *
     * @return : Retourne ma liste de musiques que je mets à disposition.
     */
    public ArrayList<Musique> updateMyMusicList()
    {
        // 1) Entrer dans répertoire upload
        // 2) Chequer tous les fichiers .wav / .mp3
        // 3) Créer MUSIQUE avec leur nom & taille de fichier
        // 4) Ajouter la musique à la liste de musiques

        // 1) Entrer dans répertoire upload
        File directoryUpload = new File(String.valueOf(Config.getConfig().getPathUpload()));

        // on va stocker tout le contenu du dossier dans une liste
        String[] fileList = directoryUpload.list();

        // Vider la liste de musique actuelle, pour remplacer par la nouvelle
        musicList.clear();


        // 2) Filtrer les élements de la liste
        for (int i = 0; i < fileList.length; i++)
        {
            // On récupère dans une String l'extension du fichier
            int extensionIndex = fileList[i].lastIndexOf(".") + 1;
            String extension = fileList[i].substring(extensionIndex);

            // Si son extention est mp3, on créé un objet musique qu'on stocke dans la liste d'objets musique
            if (extension.equals("mp3") || extension.equals("wav"))
            {
                // On récupère la taille de la musique
                File fileMusique = new File(Paths.get(directoryUpload.getPath(), fileList[i]).toString());
                long tailleMusique = fileMusique.length();

                // 3) Créer nouvelle MUSIQUE avec leur nom & taille de fichier
                Musique musique = new Musique(fileList[i], tailleMusique);
                musique.toString();

                // 4) que l'on ajoute à la liste d'objets musique
                musicList.add(musique);
            }
        }
        printListeDeMusiques();
        // On retourne la liste d'objets musique
        return musicList;
    }

    /**
     * Affiche la liste de musiques que je mets à disposition dans mon dossier upload du PC
     */
    public void printListeDeMusiques()
    {
        // On affiche la liste des musiques
        for (Musique m : musicList)
        {
            System.out.println(m.toString());
        }
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "userIp='" + userIp + '\'' +
                ", serverPort=" + serverPort +
                ", musicList=" + musicList +
                '}';
    }

    public String getUserIp()
    {
        return userIp;
    }
}