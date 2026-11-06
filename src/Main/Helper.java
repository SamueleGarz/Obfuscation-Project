package Main;

import java.io.File;

///Class that contains useful functions
public class Helper {

    public static enum TextColors{
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),
        RESET("\u001B[0m")
        ;
        public final String value;

        private TextColors(String value){
            this.value=value;
        }
    }

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
        System.out.println(TextColors.RED.value+message+TextColors.RESET.value);
    }

    ///Prints a message to System.out in the form of \<message\> (default) : \<result\> (cyan)
    /// @param message Message to print first (default color of the terminal)
    /// @param result Value to print after the message in cyan
    public static void printResultCyan(String message, String result){System.out.println(message+": "+TextColors.CYAN.value+result+TextColors.RESET.value); }

    ///Prints a message to System.out in the form of \<message\> (default) : \<result\> (green if isPositive, else red)
    /// @param message Message to print first (default color of the terminal)
    /// @param result Value to print after the message
    /// @param isPositive if true, prints result in green, else result in red
    public static void printResult(String message, String result, boolean isPositive){
        if (isPositive) {
            System.out.println(message+": "+TextColors.GREEN.value+result+TextColors.RESET.value);
        } else {
            System.out.println(message+": "+TextColors.RED.value+result+TextColors.RESET.value);
        }
    }

    ///Prints a message to System.out in the form of \<message\> (default) : \<result\> ("true" in green if isPositive, else "false" in red)
    /// @param message Message to print first (default color of the terminal)
    /// @param isPositive if true, prints "true" in green, else "false" in red
    public static void printResult(String message, boolean isPositive){
        if (isPositive) {
            System.out.println(message+": "+TextColors.GREEN.value+"true"+TextColors.RESET.value);
        } else {
            System.out.println(message+": "+TextColors.RED.value+"false"+TextColors.RESET.value);
        }
    }

    ///Checks if the given FILE is not null, a directory and is readable
    /// @param target a File type that is supposed to be a dir
    public static boolean isValidDir(File target){
        return target != null && target.isDirectory() && target.canRead();
    }

    /// Takes an array of strings to replace with a given color. If the colors array is shorter, the last color in the array will be used for all the remaining strings to substitute
    /// @param base base string to apply the replacements to
    /// @param stringToHighlight array of strings to replace
    /// @param color array of colors to used for each respective string in stringToHighlight. If this array has fewer elements, the last one will be repeated for all remaining substitutions
    /// @return Returns a string where the elements inside of stringToHighlight are placed between the color code desired and the color reset code to avoid leaks
    public static String replaceAndHighlight(String base, String[] stringToHighlight, TextColors[] color){
        String ret=base;
        for(int i=0, j=0;i< stringToHighlight.length;i++){
            ret=ret.replaceAll(stringToHighlight[i],color[j].value+stringToHighlight[i]+TextColors.RESET.value);
            if(j+1<color.length) j++;

        }
        return ret;
    }

}
