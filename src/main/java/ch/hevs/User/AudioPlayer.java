package ch.hevs.User;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
        System.out.println("AUDIO PLAYER : Play");
    }

    // Method to pause the audio
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

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    @Override
    public void run()
    {
        System.out.println("AUDIO PLAYER : is running...");
    }
}
