package BuildTools;

import Main.Helper;
import Main.commandExecute;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BuildGradle implements BuilderInterface{
    ///Gradle presence indicators
    public static final List<String> INDICATOR_FILE=List.of("settings.gradle","build.gradle","build.gradle.kts","settings.gralde.kts");

    @Override
    public BuildData apply(File target) {
        if(!Helper.isValidDir(target)) return new BuildData();
        if(isApplicable(target)){
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
        if(!Helper.isValidDir(target)) return false;
        return Files.exists(Paths.get(target.getPath().concat("/gradlew")));
    }

    @Override
    public BuildData buildWithWrapper(File target) {
        if (commandExecute.runComm(target,new String[]{"./gradlew", "build"}) == 0) {
            return new BuildData(target.toPath(),true, BuildType.GRADLE);
        }else{
            return new BuildData(target.toPath(),false, BuildType.GRADLE);
        }
    }

    @Override
    public boolean isBuildToolPresent() {
        return false;
    }

    @Override
    public BuildData buildWithoutWrapper(File target) {
        return new BuildData(target.toPath(),false, BuildType.GRADLE);
    }
}
