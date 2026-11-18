package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class commandExecute {

    static boolean VERBOSE=false;

    static void setVerbose(boolean val){
        VERBOSE=val;
    }

    public static List<String> outpStorage=new ArrayList<>();
    static List<String> errStorage=new ArrayList<>();

    static void readInto(InputStream input, List<String> holder){
        try(BufferedReader reader=new BufferedReader(new InputStreamReader(input))){
            holder.clear();
            String line;
            while ((line=reader.readLine())!=null){
                holder.add(line);
            }
        }catch (Exception e){
            Helper.printRed("Error (commandExecute): issue while reading the input stream: \n"+e);
        }
    }

    ///Given a target directory and a command, executes the command in said directory.
    ///
    /// Checks if the given target is null, a directory and readable.
    ///
    /// @param target directory in which to execute the command
    /// @param command command to execute using ProcessBuilder
    /// @param jdkPath param to decide where to set JAVA_HOME (if null, will not be set)
    /// @return result of the command execution, -1 if an issue arose
    public static int runComm(File target, String[] command, String jdkPath){
        outpStorage.clear();
        errStorage.clear();
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
            build.command(command);
            build.directory(target);

            if(jdkPath!=null){
                build.environment().put("JAVA_HOME",jdkPath);
            }

            try (Process proc = build.start()) {
                Thread outThread=new Thread(()->readInto(proc.getInputStream(),outpStorage));
                Thread errThread=new Thread(()->readInto(proc.getErrorStream(),errStorage));
                outThread.start();
                errThread.start();

                int code=proc.waitFor();
                outThread.join();
                errThread.join();
                proc.close();

                if (VERBOSE) Helper.printResult("\tCode",String.valueOf(code),code==0);

                //print any and all errors
                if (VERBOSE && code!=0) {
                    Helper.printRed("\tError log for execution on "+target.getPath()+":");
                    for (String a : errStorage){
                        Helper.printRed("\t\t"+a);
                    }
                    if(errStorage.isEmpty()){
                        for (String a : outpStorage){
                            Helper.printRed("\t\t"+a);
                        }
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
