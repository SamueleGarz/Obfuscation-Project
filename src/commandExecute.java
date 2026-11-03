import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class commandExecute {

    static boolean VERBOSE=false;

    static void setVerbose(boolean val){
        VERBOSE=val;
    }

    ///Given a target directory and a command, executes the command in said directory.
    ///
    /// Checks if the given target is null, a directory and readable.
    ///
    /// @param target directory in which to execute the command
    /// @param command command to execute using ProcessBuilder
    /// @return result of the command execution, -1 if an issue arose
    static int runComm(File target, String[] command){
        if(!System.getProperty("os.name").toLowerCase().startsWith("windows")){
            if (target==null){
                if (VERBOSE) System.err.println("\tError: Null target");
                return -1;
            }
            if (!target.isDirectory()) {
                if (VERBOSE) System.err.println("\tError: Not a directory: "+target.getName());
                return -1;
            }
            if (!target.canRead()){
                if (VERBOSE) System.err.println("\tError: Cannot read the given directory: "+target.getName());
                return -1;
            }

            ProcessBuilder build=new ProcessBuilder();
            build.command("sh","-c");
            build.command(command);
            build.directory(target);
            try (Process proc = build.start()) {
                BufferedReader readerErr=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                String lineErr;
                List<String> errStorage=new ArrayList<>();
                while ((lineErr=readerErr.readLine())!=null){
                    errStorage.add(lineErr);
                }
                int code=proc.waitFor();
                if (VERBOSE) Helper.printResult("\tCode",String.valueOf(code),code==0);

                //print any and all errors
                if (VERBOSE && code!=0) {
                    Helper.printRed("\tError log for build of "+target.getPath()+":");
                    for (String a : errStorage){
                        Helper.printRed("\t\t"+a);
                    }
                }
                return code;
            }catch (Exception e){
                Helper.printRed("Exception arose while running the command "+ Arrays.toString(command) +" in directory "+target+" :\n"+e);
            }
        }
        return -1;
    }
}
