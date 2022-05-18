package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.io.File;
import java.io.Serializable;
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
