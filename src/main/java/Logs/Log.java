package Logs;

import ch.hevs.Configurations.Config;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

public class Log {
    public Logger myLogger;
    // By month
    private LocalDate date = LocalDate.now();
    private int year = date.getYear();
    private String month = date.getMonth().toString();
    private String filePath = Paths.get(Config.getConfig().getPathLogs().toString(), "logs" + month + year + ".log").toString();

    public Log() {
        // Get logger
        myLogger = Logger.getLogger("TestLog");

        try{
            // Define a new file handler and its log
            FileHandler fh = new FileHandler(filePath, true);
            // Add the handle to the log
            myLogger.addHandler(fh);
            // Use our custom formatter
            Formater.SocketFormatter myFormatter = new Formater.SocketFormatter();
            fh.setFormatter(myFormatter);

        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

