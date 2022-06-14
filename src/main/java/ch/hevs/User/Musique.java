package ch.hevs.User;

import java.io.Serializable;

/**
 * La classe musique représente une musique sur le PC.
 * Elle sert à donner des attributs à la musique que l'on voudrait avoir.
 * Cette classe pourra être modifié dans le futur, pour ne plus s'apeller MUSIC, mais media, si on veut aussi jouer des films.
 * @implements Serializable : On va sérialiser le CLIENT, qui contient une liste de MUSIQUE.
 *                            Donc la classe MUSIQUE doit implémenter Serializable elle aussi.
 */
public class Musique implements Serializable
{
    // A T T R I B U T S
    private static final long serialVersionUID = 45L;
    private String musicFileName;
    private long musicFileSize;


    // C O N S T R U C T E U R
    public Musique(String fileName, long fileSize)
    {
        this.musicFileName = fileName;
        this.musicFileSize = fileSize;
    }

    /**
     * Donne les attributs de la musique
     * @return : String : Le nom de la musique, et sa taille
     */
    @Override
    public String toString()
    {
        return "Musique{" +
                "fileName='" + musicFileName + '\'' +
                '}';
    }

    // G E T T E R S
    public String getMusicFileName()
    {
        return musicFileName;
    }

    public long getMusicFileSize()
    {
        return musicFileSize;
    }
}

