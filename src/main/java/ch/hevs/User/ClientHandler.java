package ch.hevs.User;

import ch.hevs.Configurations.Config;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static ch.hevs.User.AppUser.log;


/**
 * Thread qui gère les requetes du client et qui envoie les réponses au client.
 * Est créé par le serveur, pour chaque client connecté.
 */
class ClientHandler implements Runnable
{
    // A T T R I B U T S
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    private boolean isRunning;
    private ArrayList<Musique> myMusicList;
    private Musique selectedMusic;


    // C O N S T R U C T E U R
    /**
     * Constructeur de la classe ClientHandler
     * @param socket : Socket du Serveur
     * @param dis : DataInputStream du Serveur
     * @param dos : DataOutputStream du Serveur
     * @param listeDeMusiques : Liste de musiques du Serveur qui elle même vient de AppUser
     */
    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos, ArrayList<Musique> listeDeMusiques)
    {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        this.myMusicList = listeDeMusiques;
        this.isRunning = true;
    }


    /**
     * Méthode qui gère les requetes du client et qui envoie les réponses au client.
     */
    @Override
    public void run()
    {
        String received;
        String sending;

        try
        {
            received = dis.readUTF();
            log.myLogger.info("A client has logged in : " + socket.toString());

            // 2) Handler en attente des requêtes du client
            while (isRunning)
            {
                boolean songIsFound = false;
                do
                {
                    //System.out.println("SERVER - Waiting for client request...");


                    // Se mettre en écoute des requetes du client
                    // Get le message du client --> Serveur READ / Client WRITE
                    received = dis.readUTF(); // On reçoit la musique voulue
                    log.myLogger.info("Musiqc request : " + received + " from " + socket.toString());

                    // 1) Le user va sélécteionner une musique à jouer, vérifier si elle existe dans mon dossier
                    //      Si elle existe, je lui envoie une confirmation WRITE,
                    //      sinon je lui envoie une erreur
                    ArrayList<Musique> listeDeMusiques = getMyUserMusicList();

                    for (Musique musique : listeDeMusiques)
                    {
                        // Si on a un fichier qui a le même nom
                        if(   musique.getMusicFileName().equals(received)   )
                        {
                            // On envoie une confirmation au client et on brise la boucle
                            sending = "SongFound";
                            //System.out.println("Song Found");
                            dos.writeUTF(sending);
                            dos.flush();
                            songIsFound = true;
                            selectedMusic = musique;
                            break;
                        }
                        // Si on arrive à la fin de la liste, et que on n'a pas trouvé de fichier avec un tel nom
                        else if(   musique.getMusicFileName().equals(listeDeMusiques.get(listeDeMusiques.size()-1).getMusicFileName())   )
                        {
                            // On envoie une erreur au client et on brise la boucle
                            sending = "SongNotFound";
                            //System.out.println("Song Not Found");
                            dos.writeUTF(sending);
                            dos.flush();
                            songIsFound = false;
                            break;
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
            System.err.println("Clienthandler : Client disconnected");
        }


        // 3) Fin du thread
        try
        {
            System.out.println("SERVEUR - Closing this connection.");
            this.socket.close();
        }
        catch (IOException e)
        {
            System.err.println("SERVEUR - run 2 : We couldn't close the connection !");
            throw new RuntimeException(e);
        }

        System.out.println("SERVEUR - End of thread.");
    }

    // M E T H O D E S
    private synchronized ArrayList<Musique> getMyUserMusicList ()
    {
        // On clone la liste, car elle peut être utilisé par plusieurs threads
        ArrayList<Musique> musiques = (ArrayList<Musique>) myMusicList.clone();
        return musiques;
    }


}

