package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.io.File;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Server implements Runnable
{
    // A T T R I B U T S
    private String ipUser;
    private static final int PORT_DU_SERVEUR = 50000;
    private ArrayList<Musique> listeDeMusiques;
    private ServerSocket listeningSocket;

    private ConsoleColors cc;


    // C O N S T R U C T E U R
    public Server()
    {
        cc = ConsoleColors.YELLOW;
        listeDeMusiques = new ArrayList<Musique>();
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
        // 1) On aura 2 threads :
        //      - un thread qui va update toute les 5 secondes MA liste de musique et l'envoyer au scanner
        //          2) Serialiser Classe OUT vers fichier.ser toutes les 5 s, et overwrite l'ancien fichier

        //      - un thread qui va recevoir les requetes d'un client et les traiter
        //          3) On va créer une socket qui va écouter sur le port 50000

    }





}
