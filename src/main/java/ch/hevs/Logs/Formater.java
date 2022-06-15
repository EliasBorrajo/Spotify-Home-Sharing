package ch.hevs.Logs;

import java.util.Date;
import java.util.logging.*;

/**
 * Classe permettant de formatter les logs avec notre propre FORMAT (date + heure + message)
 * @author Arthur Avez
 */
public class Formater
{
    //extend the current Formatter
    public static class SocketFormatter extends Formatter
    {

        public SocketFormatter()
        {
            super();
        }

        public String format(LogRecord record)
        {

            // Create a StringBuffer to contain the formatted record
            StringBuffer sb = new StringBuffer();

            // Get the date from the LogRecord and add it to the buffer
            Date date = new Date(record.getMillis());
            sb.append(date.toString());
            sb.append(";");

            sb.append(record.getSourceClassName());
            sb.append("; \t\t");

            // Get the level name and add it to the buffer
            sb.append(record.getLevel().getName());
            sb.append(";");

            sb.append(formatMessage(record));
            sb.append("\r\n");

            return sb.toString();
        }
    }
}