import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainWorker {
    public static final List<String> GRADLE_FILES=List.of("settings.gradle","build.gradle","build.gradle.kts","settings.gralde.kts");
    public static final List<String> MAVEN_FILES=List.of("mvnw","mvnw.cmd",".mvn");
    JarWorker jarWorker =new JarWorker();

    public int runComm(File target, String[] command, boolean verbose){
        if(!System.getProperty("os.name").toLowerCase().startsWith("windows")){
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
                if (verbose) System.out.println("\tCode: "+code);
                if (verbose && code!=0) {
                    System.out.println("\tError log:");
                    for (String a : errStorage){
                        System.out.println("\t\t"+a);
                    }
                }
                return code;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return -1;
    }

    //Currently checks only for Gradle and Maven and can only compile using the wrappers
    public boolean builds(File target, boolean verbose){
        boolean built=false;
        if (target==null){
            if (verbose) System.out.println("\tError: Null target");
            return false;
        }
        if (!target.isDirectory()) {
            if (verbose) System.out.println("\tError: Not a directory: "+target.getName());
            return false;
        }
        if (!target.canRead()){
            if (verbose) System.out.println("\tError: Cannot read the given directory: "+target.getName());
            return false;
        }
        boolean isGradle= GRADLE_FILES.stream().anyMatch(n -> Files.exists(Paths.get(target.getPath()+"/"+n)));
        if (verbose) System.out.println("\tIs gradle: "+isGradle);
        if (isGradle) {
            if (Files.exists(Paths.get(target.getPath() + "/gradlew"))) {
                String[] comm = new String[]{"./gradlew", "build"};
                int retcode = runComm(target, comm, verbose);
                jarWorker.jarFinder(target,"build/libs");

                if (retcode == 0) {
                    return true;
                }
            } else {
                if (verbose) System.out.println("\tError: Missing Gradle Wrapper in repository");
            }
        }
        //Could be reached in the event that the project moved from Gradle to Maven and did not clear the repo
        if (Files.exists(Paths.get(target.getPath()+"/pom.xml"))) {
            if (verbose) System.out.println("\tIs Maven: True ");
            if (MAVEN_FILES.stream().anyMatch(n -> Files.exists(Paths.get(target.getPath()+"/"+n)))){
                String[] comm=new String[]{"./mvnw","clean","package"};
                int retcode=runComm(target,comm,verbose);
                if (retcode==0){
                    return true;
                }
            }else{
                if (verbose) System.out.println("\tError: Missing Maven Wrapper in repository");
            }
        }else{
            if (verbose) System.out.println("\tIs Maven: False ");
        }
        return built;
    }

    public void main() {
        //Find the files
        File sources = new File(System.getProperty("data.dir","data")+"/Sources");

        File[] files=sources.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            // Print name of the all files present in that path
            System.out.println("- "+file.getName());
            //Check if they compile into jars
            //!!!! PLUGINS / MODS LIKE VEINMINER CAN PRODUCE MULTIPLE JARS FROM A SINGLE BUILD, EACH IN ITS OWN FOLDER !!!!!!!
            System.out.println("\tCan build: "+builds(file,true));
        }

        //inject inside the program


        //check if they compile again after injection
        //in the future possibly inject something else

        //read the new jar and look for the injection

    }
}
