package ch.hevs.ToolBox.ConsoleColors;

public class ColoredText
{
    public static void main(String[] args)
    {
        System.out.println("TESTING COLORING TEXT");
        ConsoleColors colors = ConsoleColors.BLUE;

        System.out.println(colors.BLUE.getCOLOR() + "Yolo" +ConsoleColors.ANSI_RESET.getCOLOR());
        System.out.println(colors.GREEN.getCOLOR() + "Yolo" );
        System.out.println("Should be green, because I have not use RESET COLOR");
        System.out.println(colors.BLUE.getCOLOR() + "Yolo" );
        System.out.println(colors.getCOLOR() + "should be blue");
        System.out.println(colors.YELLOW.getCOLOR() + "Yolo" +ConsoleColors.ANSI_RESET.getCOLOR());
        System.out.println("Should be default");
    }
}
