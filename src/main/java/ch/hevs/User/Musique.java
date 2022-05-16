package ch.hevs.User;

public class Musique {

    private String fileName;
    private long fileSize;

    @Override
    public String toString() {
        return "Musique{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

    public Musique (String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

}


