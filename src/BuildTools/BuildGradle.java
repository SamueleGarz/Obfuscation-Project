package BuildTools;

import Main.Helper;
import Main.commandExecute;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BuildGradle implements BuilderInterface{
    ///Gradle presence indicators
    public static final List<String> INDICATOR_FILE=List.of("settings.gradle","build.gradle","build.gradle.kts","settings.gradle.kts");

    ///Path for the downloaded version
    private Path builderLocation=null;

    private String distribution =null;

    @Override
    public BuildData apply(File target) {
        if(!Helper.isValidDir(target)) return new BuildData();
        if(isApplicable(target)){
            Properties prop=new Properties();
            try(InputStream in= Files.newInputStream(target.toPath().resolve("gradle/wrapper/gradle-wrapper.properties"))){
                prop.load(in);
            }catch(Exception e){
                Helper.printRed("Error during reading of Gradle Properties for"+target.getPath()+": "+e);
            }
            String temp=prop.getProperty("distributionUrl");

            if(temp!=null){
                Pattern pattern=Pattern.compile("/gradle-([\\d.]+)-");
                Matcher matcher=pattern.matcher(temp);
                if(matcher.find()){
                    this.distribution = matcher.group(1);
                }else{
                    this.distribution =null;
                }
            }

            if(wrapperExists(target)){
                return buildWithWrapper(target);
            }else{
                if (isBuildToolPresent()){
                    return buildWithoutWrapper(target);
                }
            }
        }
        return new BuildData(target.toPath(),false, BuildType.OTHER);
    }

    @Override
    public boolean isApplicable(File target) {
        if(!Helper.isValidDir(target)) return false;
        return INDICATOR_FILE.stream().anyMatch(n -> Files.exists(Paths.get(target + "/" + n)));
    }

    @Override
    public boolean wrapperExists(File target) {
        return Files.exists(Paths.get(target.getPath().concat("/gradlew")));
    }

    @Override
    public BuildData buildWithWrapper(File target) {
        BuildData ret=new BuildData(target.toPath(),false, BuildType.GRADLE);
        if(this.distribution ==null) return ret;

        float version=Float.parseFloat(this.distribution.substring(0,this.distribution.lastIndexOf(".")));
        String libPath=System.getProperty("user.dir");
        String jdkPath=null;
        if(version<7.3){
            Helper.printRed("Gradle Version <7.3");
            return ret;
        } else if (version<8.3) {
            if(Files.exists(Path.of(libPath+"/lib/JDKs/jdk-17.0.2"))){
                jdkPath=libPath+"/lib/JDKs/jdk-17.0.2";
            }else{
                Helper.printRed("Could not find lib/jdk-17.0.2");
            }
        }else if (version<9.1){
            if(Files.exists(Path.of(libPath+"/lib/JDKs/jdk-20.0.2"))){
                jdkPath=libPath+"/lib/JDKs/jdk-20.0.2";
            }else{
                Helper.printRed("Could not find lib/jdk-20.0.2");
            }
        }else{
            if(Files.exists(Path.of(libPath+"/lib/JDKs/jdk-25.0.2"))){
                jdkPath=libPath+"/lib/JDKs/jdk-25.0.2";
            }else{
                Helper.printRed("Could not find lib/jdk-25.0.2");
            }
        }

        if (commandExecute.runComm(target,new String[]{"./gradlew", "build"},jdkPath) == 0) {
            if(commandExecute.runComm(target,new String[]{"./gradlew","-I",libPath+"/lib/GradleParts/printcp.gradle","printCompileClasspath","-q"},jdkPath)==0){
                ret.builds=true;

                Path jmods = Path.of(jdkPath+"/jmods");
                File[] jmodFiles=new File(jmods.toUri()).listFiles();
                if(jmodFiles!=null){
                    for(File jmodFile: jmodFiles){
                        if(jmodFile.toString().endsWith(".jmod")){
                            commandExecute.outpStorage.add(jmodFile.toString().concat("(!**.jar;!module-info.class)"));
                        }
                    }
                }
                if(commandExecute.outpStorage!=null){
                    for(String a: commandExecute.outpStorage){
                        if(a.strip().endsWith(".jar") || a.endsWith("(!**.jar;!module-info.class)") || a.startsWith("CP") || a.startsWith("DEP") || a.startsWith("DIR")){
                            ret.dependenciesData.add(a);
                        }
                    }
                }

            }
        }
        return ret;
    }

    @Override
    public boolean isBuildToolPresent() {
        boolean found=false;
        try(Stream<Path> paths=Files.walk(Path.of("tools/Gradle"))){
            java.util.Optional<Path> result =paths.filter(Files::isRegularFile).filter(p -> p.getFileName().toString().equals("gradlew")).findFirst();
            found=result.isPresent();
            if(found){
                this.builderLocation=result.get();
            }
        }catch (Exception e){
            Helper.printRed("Error: an issue occurred during the check for the presence of Gradle in tools");
        }
        return found;
    }

    @Override
    public BuildData buildWithoutWrapper(File target) {
        if (this.builderLocation==null) return new BuildData(target.toPath(),false, BuildType.GRADLE);

        if (commandExecute.runComm(target,new String[]{"sh","-c",builderLocation.toString(), "build"},null) == 0) {
            return new BuildData(target.toPath(),true, BuildType.GRADLE);
        }else{
            return new BuildData(target.toPath(),false, BuildType.GRADLE);
        }
    }

    public BuildGradle(){
        try {
            Files.createDirectories(Path.of(System.getProperty("tools.dir","tools")+"/Gradle"));}
        catch (Exception e){
            Helper.printRed("Exception arose while creating the Gradle tool repository:\n"+e);
        }
    }
}
