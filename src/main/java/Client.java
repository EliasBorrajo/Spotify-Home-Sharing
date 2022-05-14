import java.net.InetAddress;
import java.util.ArrayList;

public class Client {

    private InetAddress ip;
    private int port;
    private ArrayList <Musique> listeDeMusiques = new ArrayList <Musique> ();

    public Client (InetAddress ip, int port){
        this.ip = ip;
        this.port = port;
        this.listeDeMusiques = getListeDeMusiques();
    }

    private ArrayList<Musique> getListeDeMusiques() {

        return null;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ip=" + ip +
                ", port=" + port +
                ", listeDeMusiques=" + listeDeMusiques +
                '}';
    }
}
