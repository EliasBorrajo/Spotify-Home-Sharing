package ch.hevs.User;


public class Musique
{

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

