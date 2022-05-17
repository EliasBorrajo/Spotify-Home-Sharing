package ch.hevs.User;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

public class Server implements Runnable
{
    // A T T R I B U T S
    private ConsoleColors cc = ConsoleColors.YELLOW;

    // C O N S T R U C T E U R
    public Server()
    {
        // TODO
    }

    // R U N N A B L E
    @Override
    public void run()
    {
        System.out.print(cc.YELLOW.getCOLOR());
        System.out.println("Server is running");

        startApp();

    }

    // M E T H O D E S
    private void startApp()
    {
        // 1) Aller lire les fichiers dans le répertoire, et créer une liste des fichiers à envoyer au Scanner
        // 2)
    }
}
