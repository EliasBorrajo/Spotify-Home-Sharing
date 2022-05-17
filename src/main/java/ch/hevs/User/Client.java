package ch.hevs.User;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable
{
    // A T T R I B U T S
    private InetAddress ip;
    private int port;
    private ArrayList<Musique> listeDeMusiques = new ArrayList <Musique> ();


    private ConsoleColors cc = ConsoleColors.GREEN;
    private boolean isRunningApp;

    // C O N S T R U C T E U R
    public Client(boolean isRunningApp)
    {
        this.isRunningApp = isRunningApp;
    }

    // R U N N A B L E
    @Override
    public void run()
    {
        System.out.print(cc.GREEN.getCOLOR());
        System.out.println("Client is running");
        init();
        startApp();

    }

    // M E T H O D E S
    private void init()
    {

    }

    private void startApp()
    {



        // 1) l'application console démare ici
        // 2) On doit se connecter à un Scanner, entrer les informations de connexion
        // 3) l'application console attend une commande
            // le client aura plusieurs options :
            // - afficher la liste des utilisateurs / fichiers à disposition --> Refresh liste du scanner
            // - Mettre à jour ma liste de musiques que je mets à disposition
            // - se déconnecter --> logout
            // - Ecouter de la musique en streaming --> listen dans un nouveau thread. On peut stop la music à tout moment, et continuer à utiliser l'application


        int userChoice = -1;
        System.out.println("User choice : ");
        Scanner scan = new Scanner(System.in);
        userChoice = scan.nextInt();

        switch (userChoice)
        {
            case 1:
                // 1.1) l'application console affiche le menu
                System.out.println("CHOIX 1 MAN !");
                isRunningApp = false;

                break;
            case 2:
                break;
            default:
                break;
        }

    }
}
