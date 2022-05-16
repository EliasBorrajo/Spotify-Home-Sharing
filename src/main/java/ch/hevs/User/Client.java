package ch.hevs.User;

import ch.hevs.ToolBox.ConsoleColors.ConsoleColors;

import java.util.Scanner;

public class Client implements Runnable
{
    // A T T R I B U T S
    private ConsoleColors cc = ConsoleColors.GREEN;

    // C O N S T R U C T E U R

    // R U N N A B L E
    @Override
    public void run()
    {
        System.out.print(cc.GREEN.getCOLOR());
        System.out.println("Client is running");
        init();
        startApp();

    }

    // M E T H O D E S
    private void init()
    {

    }

    private void startApp()
    {



        // 1) l'application console démmare ici
        // 2) l'application console attend une commande
        int userChoice = -1;
        System.out.println("User choice : ");
        Scanner scan = new Scanner(System.in);
        userChoice = scan.nextInt();

        switch (userChoice)
        {
            case 1:
                // 1.1) l'application console affiche le menu
                System.out.println("CHOIX 1 MAN !");
                break;
            case 2:
                break;
            default:
                break;
        }

    }
}
