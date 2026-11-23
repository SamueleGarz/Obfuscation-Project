package Main;

import BuildTools.*;
import JarTools.DetectionData;
import JarTools.InjectionData;
import JarTools.JarDetector;
import JarTools.JarInjector;
import com.sun.tools.jconsole.JConsoleContext;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainWorker {

    ///Verbose
    private boolean VERBOSE=false;
    ///0: both, 1:injection, 2:detection
    private int OPERATION=0;

    JarInjector jarInjector = new JarInjector();
    JarDetector jarDetector= new JarDetector();

    ///Array list of all the building tools to be used
    List<BuilderInterface> builders=new ArrayList<>();
    ///Loads all the builders to be used for building
    public void loadBuilder(){
        try {
            Files.createDirectories(Path.of(System.getProperty("tools.dir","tools")));}
        catch (Exception e){
            Helper.printRed("Exception arose while creating the tools repository:\n"+e);
        }

        builders.add(new BuildGradle());
        builders.add(new BuildMaven());
    }

    /// Given a target file, checks if it's not null, a directory and if it can be read. If so, checks if the jar can be built using Gradle (for now, only with the wrapper) or Maven (only wrapper)
    ///
    /// @param target the target file, should be a directory that is readable
    /// @return returns a sourceData instance. If the build fails returns an object with builds=False and type=OTHER. If the target is null, not a directory or not readable, the path is also null.
    ///
    /// TODO: Add more compilation tools, add the option to build without the wrappers
    public BuildData builds(File target){
        if(!Helper.isValidDir(target)) return new BuildData();

        BuildData temp;
        for(BuilderInterface builder:builders){
            temp=builder.apply(target);
            if(temp.builds) return temp;
        }

        return new BuildData(target.toPath(),false, BuildType.OTHER);

    }

    ///Main entry point of the tool, when called it will look for the existence of the directories System.getProperty("data.dir","data")/Jars and System.getProperty("data.dir","data")/Sources.
    /// If not present, main will attempt to create them.
    ///
    /// Following that, it will iterate over all the files under /Sources and attempt to build them; if successful, injection and detection follow
    /// @param args for now, the only argument that can be passed is for VERBOSE. accepted values are [true/false] (case-insensitive), and if not recognized it will default to false.
    ///
    /// TODO: add detection, improve injection (See JarTools.JarWorker)
    void main(String[] args) {
        //Set arguments if present
        if(args.length<1){
            Helper.printResult("Additional arguments omitted.\nCurrent arguments:\n\tVERBOSE",VERBOSE);
            Helper.printResultCyan("\tOPERATION",String.valueOf(OPERATION));
        }
        else{
            try{
                this.VERBOSE=Boolean.parseBoolean(args[0]);
                commandExecute.setVerbose(this.VERBOSE);
                //not necessary if VERBOSE=false, by default it's false
                this.jarInjector.setVERBOSE(VERBOSE);
                this.jarDetector.setVERBOSE(VERBOSE);
            } catch (Exception e) {
                Helper.printRed("Argument invalid for VERBOSE\nArgument passed: "+args[0]+"\nExpected: [true/false] (case-insensitive)\nCurrent arguments:\n\tVERBOSE="+VERBOSE);
            }
            if(args.length<2){
                Helper.printResult("Some additional arguments were omitted.\nCurrent arguments:\n\tVERBOSE",VERBOSE);
                Helper.printResultCyan("\tOPERATION",String.valueOf(OPERATION));

            }else{
                try{
                    this.OPERATION=Integer.parseInt(args[1]);
                } catch (Exception e) {
                    Helper.printRed("Argument invalid for OPERATION\nArgument passed: "+args[1]+"\nExpected: [0/1/2] (Both/Only Injection/Only Detection)\nCurrent arguments:\n\tOPERATION="+OPERATION);
                }
            }
        }

        // Check if the folders exists and create them if not
        try {
            Files.createDirectories(Path.of(System.getProperty("data.dir","data")+"/Jars"));
            Files.createDirectories(Path.of(System.getProperty("data.dir","data")+"/Sources"));
        } catch (Exception e){
            Helper.printRed("Exception arose while creating the /Jars and /Sources repositories:\n"+e);
        }

        File sources = new File(System.getProperty("data.dir","data")+"/Sources");

        //Find the projects inside the data/Sources folder
        File[] sourceFiles=sources.listFiles();
        if (sourceFiles == null) {
            return;
        }

        //loads all the builder tools into the array list
        loadBuilder();


        //the injection module is dependent on being able to build a certain file, so that's why the check for building
        //is first
        //-------------------------------------------------------------------------------------------------------------
        if(OPERATION<2){
            List<InjectionData> injectionData = new ArrayList<>();
            for (File file : sourceFiles) {
                // Print name of each file in the data/Sources folder
                if(VERBOSE) Helper.print("- "+file.getName());

                //Check if each compiles properly with one of the methods
                BuildData buildRes=builds(file);

                if(VERBOSE) Helper.printResult("\tCan build",buildRes.builds);
                if(buildRes.builds) {
                    //inject inside the program
                    try {
                        Files.walk(buildRes.sourcePath).filter(path -> path.toString().endsWith(".jar") &&
                                path.getParent().toString().contains(buildRes.buildType.buildDir)).forEach(path -> injectionData.add(jarInjector.jarExplorer(path,buildRes)));
                    } catch (Exception e) {
                        Helper.printRed("Exception arose during the walk of Path " + buildRes.sourcePath + ": \n" + e);
                    }

                }
            }
            Helper.print("Injection Data: ");
            for(InjectionData d:injectionData){
                d.printNonEmpty("\t");
            }
        }

        if(OPERATION==0 || OPERATION==2){
            //Detection module
            //-------------------------------------------------------------------------------------------------------------
            File jars = new File(System.getProperty("data.dir","data")+"/Jars");
            File[] jarFiles=jars.listFiles();
            if (jarFiles == null) {
                return;
            }

            List<DetectionData> detectionData=new ArrayList<>();
            for(File file: jarFiles){
                DetectionData temp=new DetectionData(file.toPath());
                jarDetector.jarExplorer(temp);
                detectionData.add(temp);
            }


            if(VERBOSE){
                Helper.print("Detection Data:");
                for(DetectionData d:detectionData){
                    d.printNonEmpty("\t");
                }
            }

            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss") ;
            try(FileWriter writer=new FileWriter(System.getProperty("data.dir","data")+"/Outputs/Detection/"+dateFormat.format(date)+".json")){
                writer.write("{\"results\":[\n");
                boolean notSingle=false;
                for(DetectionData d:detectionData){
                    if(notSingle) writer.write(",\n");
                    else writer.write("\n");
                    writer.write(d.getJson());
                    notSingle=true;
                }
                writer.write("]}");
            } catch (Exception e) {
                Helper.printRed("Error while writing detection data into "+System.getProperty("data.dir","data")+"/Outputs/Detection/"+dateFormat.format(date)+".json"+":\n"+e);
            }
        }

    }
}
