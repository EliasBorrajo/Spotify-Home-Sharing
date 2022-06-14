package ch.hevs.User;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe permettant de jouer une musique via un InputStream, sans avoir besoin de stocker la musique dans un fichier.
 * La classe AudioSystem utilise un "clip" qui va nous servir pour savoir à quelle seconde de la musique on se trouve,
 * et ainsi pouvoir utiliser des commandes tel que PLAY PAUSE et STOP.
 * @implements Runnable : Permet de lancer le thread de l'écoute de musique, et un autre thread pourra commander de la musique.
 *
 * @author Antoine Wiedmer, Elias Borrajo
 */
public class AudioPlayer implements Runnable
{
    // A T T R I B U T S
    // to store current position
    private Long currentFrame;
    private Clip clip;

    // current status of clip
    private String status;

    private AudioInputStream audioInputStream;
    static String filePath;


    // C O N S T R U C T E U R

    /**
     * Constructeur de la classe AudioPlayer.
     * @param is : Lui donne l'InputStream de la musique à jouer.
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public AudioPlayer(InputStream is) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        // create AudioInputStream object
        //On fait toujours inputStream depuis le socket, peu importe client / serveur
        audioInputStream = AudioSystem.getAudioInputStream(is);

        //AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();  // Va lire sur le stream

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        clip.loop(Clip.LOOP_CONTINUOUSLY); // Joue la musique en boucle
    }

    /**
     * Joue la musique
     */
    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
        System.out.println("AUDIO PLAYER : Play");
    }

    /**
     * Arrête la musique
     */
    public void pause()
    {
        if (status.equals("paused"))
        {
            System.out.println("AUDIO PLAYER : Audio is already paused");
            return;
        }
        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
        System.out.println("AUDIO PLAYER : Pause");
    }

    /**
     * Arrête la musique et remet le curseur à la position 0
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    /**
     * Thread de l'écoute de la musique.
     */
    @Override
    public void run()
    {
        System.out.println("AUDIO PLAYER : is running...");
    }
}
