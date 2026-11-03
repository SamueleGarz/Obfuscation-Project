package Main;

import java.io.File;

///Class that contains useful functions
public class Helper {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    ///Print a message to System.out, default
    public static void print(String message){
        System.out.println(message);
    }

    ///Print a message to System.out in RED
    public static void printRed(String message){
        System.out.println(ANSI_RED+message+ANSI_RESET);
    }

    ///Prints a message to System.out in the form of \<message\> (default) : \<result\> (cyan)
    /// @param message Message to print first (default color of the terminal)
    /// @param result Value to print after the message in cyan
    public static void printResultCyan(String message, String result){System.out.println(message+": "+ANSI_CYAN+result+ANSI_RESET); }

    ///Prints a message to System.out in the form of \<message\> (default) : \<result\> (green if isPositive, else red)
    /// @param message Message to print first (default color of the terminal)
    /// @param result Value to print after the message
    /// @param isPositive if true, prints result in green, else result in red
    public static void printResult(String message, String result, boolean isPositive){
        if (isPositive) {
            System.out.println(message+": "+ANSI_GREEN+result+ANSI_RESET);
        } else {
            System.out.println(message+": "+ANSI_RED+result+ANSI_RESET);
        }
    }

    ///Prints a message to System.out in the form of \<message\> (default) : \<result\> ("true" in green if isPositive, else "false" in red)
    /// @param message Message to print first (default color of the terminal)
    /// @param isPositive if true, prints "true" in green, else "false" in red
    public static void printResult(String message, boolean isPositive){
        if (isPositive) {
            System.out.println(message+": "+ANSI_GREEN+"true"+ANSI_RESET);
        } else {
            System.out.println(message+": "+ANSI_RED+"false"+ANSI_RESET);
        }
    }

    ///Checks if the given FILE is not null, a directory and is readable
    /// @param target a File type that is supposed to be a dir
    public static boolean isValidDir(File target){
        return target != null && target.isDirectory() && target.canRead();
    }

}
