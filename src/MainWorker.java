import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWorker {

    private boolean VERBOSE=false;

    ///Gradle presence indicators
    public static final List<String> GRADLE_FILES=List.of("settings.gradle","build.gradle","build.gradle.kts","settings.gralde.kts");
    ///Maven presence indicators
    public static final List<String> MAVEN_FILES=List.of("pom.xml");

    JarWorker jarWorker =new JarWorker();

    ///Given a target directory and a command, executes the command in said directory.
    ///
    /// Checks if the given target is null, a directory and readable.
    ///
    /// @param target directory in which to execute the command
    /// @param command command to execute using ProcessBuilder
    /// @return result of the command execution, -1 if an issue arose
    public int runComm(File target, String[] command){
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
                if (VERBOSE) System.out.println("\tCode: "+code);

                //print any and all errors
                if (VERBOSE && code!=0) {
                    System.err.println("\tError log:");
                    for (String a : errStorage){
                        System.err.println("\t\t"+a);
                    }
                }
                return code;
            }catch (Exception e){
                System.err.println("Exception arose while running the command "+Arrays.toString(command) +" in directory "+target+" :\n"+e);
            }
        }
        return -1;
    }

    //Currently checks only for Gradle and Maven and can only compile using the wrappers

    /// Given a target file, checks if it's not null, a directory and if it can be read. If so, checks if the jar can be built using Gradle (for now, only with the wrapper) or Maven (only wrapper)
    ///
    /// @param target the target file, should be a directory that is readable
    /// @return returns a sourceData instance. If the build fails returns an object with builds=False and type=OTHER. If the target is null, not a directory or not readable, the path is also null.
    ///
    /// TODO: Add more compilation tools, add the option to build without the wrappers
    public sourceData builds(File target){
        //checks for existence and readability
        //Should never be null based on default execution, but
        //to avoid future issues it's better to add it
        if (target==null){
            if (VERBOSE) System.err.println("\tError: Null target");
            return new sourceData();
        }
        if (!target.isDirectory()) {
            if (VERBOSE) System.err.println("\tError: Not a directory: "+target.getName());
            return new sourceData();
        }
        if (!target.canRead()){
            if (VERBOSE) System.err.println("\tError: Cannot read the given directory: "+target.getName());
            return new sourceData();
        }

        String path=target.getPath();
        String[] comm=null;
        int retCode=-1;
        Type retType=Type.OTHER;

        //Gradle check and build
        if (GRADLE_FILES.stream().anyMatch(n -> Files.exists(Paths.get(target.getPath()+"/"+n)))) {
            if (VERBOSE) System.out.println("\tGRADLE_FILES Found");
            retType=Type.GRADLE;
            //Build using the Gradle wrapper if present
            if (Files.exists(Paths.get(path.concat( "/gradlew")))) {
                comm = new String[]{"./gradlew", "build"};
            //TODO: add the option to build when the wrapper is missing. Requires installing multiple versions for compatibility?
            } else {
                if (VERBOSE) System.out.println("\tMissing Gradle Wrapper in repository");
            }
        }
        //Could be reached in the event that the project moved from Gradle to Maven and did not clear the repo
        if (MAVEN_FILES.stream().anyMatch(n -> Files.exists(Paths.get(target.getPath()+"/"+n)))) {
            if (VERBOSE) System.out.println("\tMAVEN_FILES Found");
            retType=Type.MAVEN;
            //Build using the Maven wrapper if present
            if (Files.exists(Paths.get(path.concat( "/mvn")))){
                comm=new String[]{"./mvnw","clean","package"};
            //TODO: add the option to build when the wrapper is missing. Requires installing multiple versions for compatibility?
            }else{
                if (VERBOSE) System.out.println("\tMissing Maven Wrapper in repository");
            }
        }

        if (comm!=null){
            retCode = runComm(target, comm);
        }

        //check if it was built successfully
        if (retCode == 0) {
            return new sourceData(Path.of(path),true,retType);
        }else{
            return new sourceData(Path.of(path),false,retType);
        }
    }

    ///Main entry point of the tool, when called it will look for the existence of the directories System.getProperty("data.dir","data")/Jars and System.getProperty("data.dir","data")/Sources.
    /// If not present, main will attempt to create them.
    ///
    /// Following that, it will iterate over all the files under /Sources and attempt to build them; if successful, injection and detection follow
    /// @param args for now, the only argument that can be passed is for VERBOSE. accepted values are [true/false] (case-insensitive), and if not recognized it will default to false.
    ///
    /// TODO: add detection, improve injection (See JarWorker)
    void main(String[] args) {

        //Set arguments if present
        if(args.length<1){
            System.out.println("Additional arguments omitted.\nCurrent arguments:\n\tVERBOSE="+VERBOSE);
        }
        else{
            try{
                this.VERBOSE=Boolean.parseBoolean(args[0]);
                //not necessary if VERBOSE=false, by default it's false
                this.jarWorker.setVERBOSE(VERBOSE);
            } catch (Exception e) {
                System.err.println("Argument invalid for VERBOSE\nArgument passed: "+args[0]+"\nExpected: [true/false] (case-insensitive)\nCurrent arguments:\n\tVERBOSE="+VERBOSE);
            }
        }

        //Find the files
        try {
            Files.createDirectories(Path.of(System.getProperty("data.dir","data")+"/Jars"));
            Files.createDirectories(Path.of(System.getProperty("data.dir","data")+"/Sources"));
        } catch (Exception e){
            System.err.println("Exception arose while creating the /Jars and /Sources repositories:\n"+e);
        }

        File sources = new File(System.getProperty("data.dir","data")+"/Sources");
        File[] files=sources.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            // Print name of the all files present in that path
            if(VERBOSE) System.out.println("- "+file.getName());
            //Check if they compile into jars
            sourceData buildRes=builds(file);
            if(VERBOSE) System.out.println("\tCan build: "+buildRes.builds);
            if(buildRes.builds){
                jarWorker.jar_finder(buildRes.sourcePath,buildRes.buildDir());
                //inject inside the program

                //check if they compile again after injection
                //in the future possibly inject something else

                //read the new jar and look for the injection
            }
        }
    }
}
