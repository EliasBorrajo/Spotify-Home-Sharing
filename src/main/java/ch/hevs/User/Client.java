package ch.hevs.User;

import ch.hevs.configurations.Config;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Client {

    private InetAddress ip;
    private int port;
    private ArrayList <Musique> listeDeMusiques = new ArrayList <Musique> ();
    private String pathDownload = new String("download");
    private String pathUpload = new String("upload");

    public Client (InetAddress ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public ArrayList<Musique> getListeDeMusiques() {
        // 1) Entrer dans répertoire upload
        // 2) Chequer tous les fichiers .wav / .mp3
        // 3) Créer MUSIQUE avec leur nom & taille de fichier
        // 4) Ajouter la musique à laliste de musiques

        // 1)
        // Variable qui récupère le chemin du dossier VSSPOTIFY
        String pathVsspotify = Config.getConfig().getStorePath();
        // Variable qui récupère le chemin du sous-dossier UPLOAD de VSSPOTIFY
        Path upload = Paths.get(pathVsspotify, "upload");
        // Variable qui permet de créer un fichier upload...
        File directory = new File(String.valueOf(upload));
        // ... dont on va stocker tout le contenu dans une liste
        String[] fileList = directory.list();
        // Liste qui va contenir tous les objets musique du répertoire upload
        ArrayList <Musique> listeMusiques = new ArrayList<Musique>();
        // On parcourt tous les éléments de la liste
        for (int i = 0; i < fileList.length; i++) {
            // On récupère dans une String l'extension du fichier
            int extensionIndex = fileList[i].lastIndexOf(".") + 1;
            String extension = fileList[i].substring(extensionIndex);
            // 2
            // Si son extention est mp3, on créé un objet musique qu'on stocke dans la liste d'objets musique
            if (extension.equals("mp3")) {
                // On récupère dans un long la taille de la musique
                File fileMusique = new File(Paths.get(directory.getPath(), fileList[i]).toString());
                long tailleMusique = fileMusique.length();
                // 3
                // On créé une nouvelle musique...
                Musique musique = new Musique(fileList[i], tailleMusique);
                // 4
                // ... que l'on ajoute à la liste d'objets musique
                listeMusiques.add(musique);
            }
        }
        // On retourne la liste d'objets musique
        return listeMusiques;
    }
}
