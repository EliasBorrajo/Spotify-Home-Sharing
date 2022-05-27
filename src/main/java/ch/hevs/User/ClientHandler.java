package ch.hevs.User;

import ch.hevs.Configurations.Config;
import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;
import ch.hevs.User.Client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

// ClientHandler class
class ClientHandler implements Runnable
{
    // A T T R I B U T S
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    private boolean isRunning;
    private ArrayList<Musique> myMusicList;
    private Musique selectedMusic;
    private ConsoleColors cc;


    // C O N S T R U C T E U R
    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos, ArrayList<Musique> listeDeMusiques)
    {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        this.myMusicList = listeDeMusiques;
        this.isRunning = true;
        this.cc = ConsoleColors.YELLOW;
    }


    @Override
    public void run()
    {
        String received;
        String sending;

        try
        {
            received = dis.readUTF();
            System.out.println("un client s'est connecté : " + socket.toString());

            // 2) Handler en attente des requêtes du client
            while (isRunning)
            {
                boolean songIsFound = false;
                do
                {
                    System.out.println(cc.YELLOW.getCOLOR() +
                            "SERVER - Waiting for client requests...");


                    // Se mettre en écoute des requetes du client
                    // Get le message du client --> Serveur READ / Client WRITE
                    received = dis.readUTF(); // On reçoit la musique voulue


                    // 1) Le user va sélécteionner une musique à jouer, vérifier si elle existe dans mon dossier
                    //      Si elle existe, je lui envoie une confirmation WRITE,
                    //      sinon je lui envoie une erreur
                    ArrayList<Musique> listeDeMusiques = getMyUserMusicList();
                    System.out.println("Recherche de la musique : " + received);

                    for (Musique musique : listeDeMusiques)
                    {
                        // Si on a un fichier qui a le même nom
                        if(   musique.getMusicFileName().equals(received)   )
                        {
                            // On envoie une confirmation au client et on brise la boucle
                            sending = "SongFound";
                            System.out.println("Song Found");
                            dos.writeUTF(sending);
                            dos.flush();
                            songIsFound = true;
                            selectedMusic = musique;
                            break;
                        }
                        // Si on arrive à la fin de la liste, et que on n'a pas trouvé de fichier avec un tel nom
                        else if ( (listeDeMusiques.size() - listeDeMusiques.indexOf(musique)) == 0)
                        {
                            // autrement rien n'a été trouvé, on envoie une erreur
                            System.out.println("Song not found");
                            sending = "SongNOTFound";
                            dos.writeUTF(sending);
                            dos.flush();
                            songIsFound = false;
                        }
                    }
                }while (songIsFound == false);


                // 2) Lire le fichier de la musique sur le PC
                // préparer dans la mémoire une zone dans laquelle on va mettre tout le fichier dedans
                byte[] musicBuffer = new byte[ (int) selectedMusic.getMusicFileSize() ];
                Path musicPath = Paths.get(Config.getConfig().getPathUpload().toString(), selectedMusic.getMusicFileName());
                BufferedInputStream bis = new BufferedInputStream( new FileInputStream( String.valueOf(musicPath) ));

                // Je veux que tu lises tout le fichier qui est dans un tableau de bytes, de 0 à la fin du tableau
                bis.read(musicBuffer, 0, musicBuffer.length); // maintenant notre fichier se trouve dans la RAM
                // notre fichier voyage du HDD à la RAM via le musicBuffer

                // 3) Envoyer le fichier au client
                OutputStream os = socket.getOutputStream();
                os.write(musicBuffer, 0, musicBuffer.length);
                os.flush();

            }

        }
        catch (IOException e)
        {
            System.err.println("Clienthandler : Erreur de lecture du flux de données");
            throw new RuntimeException(e);
        }


        // 3) Fin du thread
        try
        {
            System.out.println(cc.PURPLE.getCOLOR() +
                    "SCANNER - Closing this connection.");
            this.socket.close();
        }
        catch (IOException e)
        {
            System.err.println("SCANNER - run 2 : On n'a pas pu fermer la connexion !");
            throw new RuntimeException(e);
        }

        System.out.println(cc.PURPLE.getCOLOR() +
                "SCANNER - End of thread.");
    }

    // M E T H O D E S
    private synchronized ArrayList<Musique> getMyUserMusicList ()
    {
        // On clone la liste, car elle peut être utilisé par plusieurs threads
        ArrayList<Musique> musiques = (ArrayList<Musique>) myMusicList.clone();
        return musiques;
    }


}

