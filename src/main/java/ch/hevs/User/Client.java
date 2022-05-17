package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable
{
    // A T T R I B U T S
    private String ipClient;
    private int portClient;
    private ArrayList<Musique> listeDeMusiques;
    private boolean isConnected;

    private ConsoleColors cc;


    // C O N S T R U C T E U R
    public Client()
    {
        listeDeMusiques = new ArrayList<Musique>();
        cc = ConsoleColors.GREEN;
        isConnected = false;
    }

    // R U N N A B L E
    @Override
    public void run()
    {
        System.out.print(cc.GREEN.getCOLOR());
        System.out.println("Client is running");

        startApp();
    }

    // M E T H O D E S

    /**
     * Menu du client pour :
     *      Se connecter au scanner du subnet
     *      choisir la musique d'un autre user à streamer
     *      Updater ma liste à disposition du scanner
     *      Se déconnecter du scanner
     */
    private void startApp()
    {
        // 1) On doit se connecter à un Scanner, entrer les informations de connexion
        // 2) On envoie ma liste des musiques au Scanner
        // 3) l'application console attend une commande
        // le client aura plusieurs options :
        // - afficher la liste des utilisateurs / fichiers à disposition --> Refresh liste du scanner
        // - Mettre à jour ma liste de musiques que je mets à disposition
        // - se déconnecter --> logout
        // - Ecouter de la musique en streaming --> listen dans un nouveau thread. On peut stop la music à tout moment, et continuer à utiliser l'application

        // 1) On doit se connecter au scanner
        do
        {
            System.out.println("Try to connect to Scanner...");
            System.out.println("Enter your IP address : ");
            Scanner sc = new Scanner(System.in);
            //String ipServer = sc.nextLine(); // TODO : Decommenter
            String ipServer = "127.0.0.1"; // Adresse IP de mon propre PC qui est aussi mon serveur

            System.out.println("Enter your port : ");
            //int portServer = sc.nextInt(); // TODO : Decommenter
            int portServer = 45000;
            System.out.println();

            try
            {
                connectToScanner(ipServer, portServer);
            }
            catch (Exception e)
            {
                System.out.println( "ERREUR DE CONNECTION AU SCANNER, " +
                                    "veuillez vérifier les informations de connexion & réessayer");

            }
        }while (isConnected == false);

        // 2) On envoie ma liste de musiques au scanner
        System.out.println("Envoi de ma liste de musiques au scanner...");
        //sendListeDeMusiques(); // On doit envoyer la liste de musiques au scanner ? ou la GET depuis le scanner à chaque nouvel utilisateur ??

        boolean isRunning = true;
        int userChoice;
        Scanner scan = new Scanner(System.in);

        do
        {
            System.out.println("Choix de l'utilisateur : ");
            System.out.println("1) Show musics to stream");
            System.out.println("2) Update my music list");
            System.out.println("3) Logout");

            // Verify that input is an int !
            while (!scan.hasNextInt()) // If not an int !
            {
                System.out.println("This option does not exist, try again");
                scan.next();
            }
            userChoice = scan.nextInt();

            switch (userChoice)
            {
                case 1:
                    System.out.println("SELECTED : Option 1 - Show musics to stream :");
                    showMusicsToStream();

                    break;

                case 2:
                    System.out.println("SELECTED : Option 2 - Update my music list :");
                    getUpdatedListeDeMusiques();
                    printListeDeMusiques();
                    // serialiser liste ?
                    break;

                case 3:
                    System.out.println("SELECTED : Option 3 - Logout :");
                    break;

                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
            System.out.println();

            System.out.println("DO YOU WANT TO CONTINUE TO USE VS_SPOTIFY APP ?");
            // Verify that input is a boolean !
            while (!scan.hasNextBoolean())
            {
                System.out.println("Choose between true / false !");
                scan.next();
            }
            isRunning = scan.nextBoolean();


        }while (isRunning == true);

        System.out.println("Goodbye !");

    }

    private void showMusicsToStream()
    {
        System.out.println("TODO : Show musics to stream here...");
    }

    /**
     * Connexion au scanner pour récupérer la liste de musiques à streamer
     * @param ipServeur
     * @param portDuServeur
     */
    private void connectToScanner(String ipServeur, int portDuServeur)
    {
        // Tentative de connection au serveur
        try {
            Socket socket = new Socket(ipServeur, portDuServeur);

            // Confirmation de connection
            System.out.println("Je suis connecté au Scanner");
            isConnected = true;

            System.out.println("Mon adresse IP est : " + InetAddress.getLocalHost().getHostAddress());
            ipClient = InetAddress.getLocalHost().getHostAddress();

        }
        catch (IOException e)
        {
            // TODO : Gerer le cas ou il n'a pas de SCANNER sur le réseau
            throw new RuntimeException(e);
        }


    }

    /**
     * Cette methode GET, va aller voir le répertoire UPLOAD de mon PC
     * et va mettre à jour ma liste de musique à distribuer.
     * @return : Retourne ma liste de musiques que je mets à disposition.
     */
    public ArrayList<Musique> getUpdatedListeDeMusiques()
    {
        // 1) Entrer dans répertoire upload
        // 2) Chequer tous les fichiers .wav / .mp3
        // 3) Créer MUSIQUE avec leur nom & taille de fichier
        // 4) Ajouter la musique à la liste de musiques

        // 1) Entrer dans répertoire upload
        File directoryUpload = new File( String.valueOf(  Config.getConfig().getPathUpload()  ) );

        // on va stocker tout le contenu du dossier dans une liste
        String[] fileList = directoryUpload.list();

        // Vider la liste de musique actuelle, pour remplacer par la nouvelle
        listeDeMusiques.clear();


        // 2) Filtrer les élements de la liste
        for (int i = 0; i < fileList.length; i++)
        {
            // On récupère dans une String l'extension du fichier
            int extensionIndex = fileList[i].lastIndexOf(".") + 1;
            String extension = fileList[i].substring(extensionIndex);

            // Si son extention est mp3, on créé un objet musique qu'on stocke dans la liste d'objets musique
            if (extension.equals("mp3"))
            {
                // On récupère la taille de la musique
                File fileMusique = new File(Paths.get(directoryUpload.getPath(), fileList[i]).toString());
                long tailleMusique = fileMusique.length();

                // 3) Créer nouvelle MUSIQUE avec leur nom & taille de fichier
                Musique musique = new Musique(fileList[i], tailleMusique);
                musique.toString();

                // 4) que l'on ajoute à la liste d'objets musique
                listeDeMusiques.add(musique);
            }
        }
        // On retourne la liste d'objets musique
        return listeDeMusiques;
    }

    /**
     * Affiche la liste de musiques que je mets à disposition dans mon dossier upload du PC
     */
    public void printListeDeMusiques()
    {
        // On affiche la liste des musiques
        for (Musique m : listeDeMusiques)
        {
            System.out.println(m.toString());
        }
    }

}
