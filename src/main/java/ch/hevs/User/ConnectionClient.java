package ch.hevs.User;

import ch.hevs.configurations.Config;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionClient {

    public static void main(String[] args) {

        // Permet de vérifier que les dossiers de la variable d'environnement soient bien existants / créés (s'ils
        // n'existent pas encore)
        Config.getConfig();

        // Paramètres à utiliser pour se connecter au serveur
        String ipServeur = "127.0.0.1"; // Adresse IP de mon propre PC qui est aussi mon serveur
        int portDuServeur = 45000;

        // Tentative de connection au serveur
        try {
            Socket socket = new Socket(ipServeur, portDuServeur);
            // Confirmation console de connection
            System.out.println("Je suis connecté");



            // Chopper la liste de musiques dispos des autres clients sur le serveur

            // Dans le cas ou il est impossible de se connecter au serveur
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
