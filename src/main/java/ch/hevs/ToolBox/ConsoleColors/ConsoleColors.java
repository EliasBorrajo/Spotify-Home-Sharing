package ch.hevs.ToolBox.ConsoleColors;

/**
 * SOURCE : How to Print Colored Text in Java Console?
 * https://www.geeksforgeeks.org/how-to-print-colored-text-in-java-console/
 *
 * System.out.println(ANSI_COLORNAME + "This text is colored" + ANSI_RESET);
 * 1) use the wanted COLOR
 * 2) Reset after using, otherwise console colors are broken
 */
public enum ConsoleColors
{
    BLACK ("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PUPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    BLACK_BACKGROUND("\u001B[40m"),
    RED_BACKGROUND("\u001B[41m"),
    GREEN_BACKGROUND("\u001B[42m"),
    YELLOW_BACKGROUND("\u001B[43m"),
    BLUE_BACKGROUND("\u001B[44m"),
    PURPLE_BACKGROUND("\u001B[45m"),
    CYAN_BACKGROUND("\u001B[46m"),
    WHITE_BACKGROUND("\u001B[47m"),

    ANSI_RESET("\u001B[0m"); //The ANSI_RESET code turns off all ANSI attributes set so far, which should return the console to its defaults.


    // Attribut correspondand à la valeur des couleurs.
    private final String COLOR;

    ConsoleColors(String colorChoice)
    {
        this.COLOR = colorChoice;
    }

    public String getCOLOR()
    {
        return COLOR;
    }
}
