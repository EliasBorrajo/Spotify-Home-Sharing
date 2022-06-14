package ch.hevs.Logs;

import ch.hevs.Configurations.Config;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Classe qui va permettre d'écrire des logs dans un fichier.
 * @author Arthur Avez
 */
public class Log
{
    // A T T R I B U T S
    public Logger myLogger;
    // By month
    private LocalDate date = LocalDate.now();
    private int year = date.getYear();
    private String month = date.getMonth().toString();
    private String filePath = Paths.get(Config.getConfig().getPathLogs().toString(), "logs" + month + year + ".log").toString();

    // C O N S T R U C T E U R
    /**
     * Constructeur de la classe Log
     */
    public Log()
    {
        // Get logger
        myLogger = Logger.getLogger("ch.hevs");

        try
        {
            // Define a new file handler and its log
            FileHandler fh = new FileHandler(filePath, true);
            // Add the handle to the log
            myLogger.addHandler(fh);
            // Use our custom formatter
            Formater.SocketFormatter myFormatter = new Formater.SocketFormatter();
            fh.setFormatter(myFormatter);

        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}

