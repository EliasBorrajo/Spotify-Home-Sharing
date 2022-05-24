package ch.hevs.ToolBox.SleepTimerConverter;

public abstract class Converter
{

    public static int convertMStoSeconds(int miliseconds)
    {
        return miliseconds / 1000;
    }

    public static int convertMStoMinutes(int miliseconds)
    {
        return miliseconds / 1000 / 60;
    }

}
