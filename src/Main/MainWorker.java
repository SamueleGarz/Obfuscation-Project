package Main;

import BuildTools.BuildData;
import BuildTools.BuildGradle;
import BuildTools.BuildMaven;
import BuildTools.BuildType;
import JarTools.JarData;
import JarTools.JarDetector;
import JarTools.JarInjector;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainWorker {

    private boolean VERBOSE=false;

    ///Gradle builder
    BuildGradle buildGradle=new BuildGradle();

    ///Maven Builder
    BuildMaven buildMaven=new BuildMaven();

    JarInjector jarInjector= new JarInjector();
    JarDetector jarDetector= new JarDetector();

    /// Given a target file, checks if it's not null, a directory and if it can be read. If so, checks if the jar can be built using Gradle (for now, only with the wrapper) or Maven (only wrapper)
    ///
    /// @param target the target file, should be a directory that is readable
    /// @return returns a sourceData instance. If the build fails returns an object with builds=False and type=OTHER. If the target is null, not a directory or not readable, the path is also null.
    ///
    /// TODO: Add more compilation tools, add the option to build without the wrappers
    public BuildData builds(File target){
        if(!Helper.isValidDir(target)) return new BuildData();

        BuildData temp=buildGradle.apply(target);
        if(temp.builds) return temp;

        temp=buildMaven.apply(target);
        if(temp.builds) return temp;

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
        File[] files=sources.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            // Print name of each file in the data/Sources folder
            if(VERBOSE) Helper.print("- "+file.getName());

            //Check if each compiles properly with one of the methods
            BuildData buildRes=builds(file);

            if(VERBOSE) Helper.printResult("\tCan build",buildRes.builds);
            if(buildRes.builds){
                List<JarData> data=new ArrayList<>();

                //inject inside the program
                try{
                    Files.walk(buildRes.sourcePath).filter(path -> path.toString().endsWith(".jar") && path.getParent().toString().contains(buildRes.buildType.buildDir)).forEach(path -> data.add(jarInjector.jarExplorer(path)));
                } catch (Exception e){
                    Helper.printRed("Exception arose during the walk of Path "+buildRes.sourcePath+": \n"+e);
                }

                for(JarData element:data){
                    jarDetector.jarExplorer(element);
                    if(VERBOSE) element.printNonEmpty("\t");
                }


                //check if they compile again after injection
                //in the future possibly inject something else

                //read the new jar and look for the injection
            }
        }
    }
}
