package ch.hevs.User;


import java.io.Serializable;

public class Musique implements Serializable
{
    private static final long serialVersionUID = 45L;
    private String musicFileName;
    private long musicFileSize;



    public Musique(String fileName, long fileSize)
    {
        this.musicFileName = fileName;
        this.musicFileSize = fileSize;
    }

    @Override
    public String toString()
    {
        return "Musique{" +
                "fileName='" + musicFileName + '\'' +
                ", fileSize=" + musicFileSize +
                '}';
    }

    public String getMusicFileName()
    {
        return musicFileName;
    }

    public long getMusicFileSize()
    {
        return musicFileSize;
    }
}

