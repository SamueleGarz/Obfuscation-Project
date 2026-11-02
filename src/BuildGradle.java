import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BuildGradle implements BuilderInterface{
    ///Gradle presence indicators
    public static final List<String> INDICATOR_FILE=List.of("settings.gradle","build.gradle","build.gradle.kts","settings.gralde.kts");

    @Override
    public SourceData apply(File target) {
        if(target==null || !target.isDirectory() || !target.canRead()) return new SourceData();
        if(isApplicable(target)){
            if(wrapperExists(target)){
                return buildWithWrapper(target);
            }else{
                if (isBuildToolPresent()){
                    return buildWithoutWrapper(target);
                }
            }
        }
        return new SourceData(target.toPath(),false,Type.OTHER);
    }

    @Override
    public boolean isApplicable(File target) {
        if(target==null || !target.isDirectory() || !target.canRead()) return false;
        return INDICATOR_FILE.stream().anyMatch(n -> Files.exists(Paths.get(target + "/" + n)));
    }

    @Override
    public boolean wrapperExists(File target) {
        if(target==null || !target.isDirectory() || !target.canRead()) return false;
        return Files.exists(Paths.get(target.getPath().concat("/gradlew")));
    }

    @Override
    public SourceData buildWithWrapper(File target) {
        if (commandExecute.runComm(target,new String[]{"./gradlew", "build"}) == 0) {
            return new SourceData(target.toPath(),true,Type.GRADLE);
        }else{
            return new SourceData(target.toPath(),false,Type.GRADLE);
        }
    }

    @Override
    public boolean isBuildToolPresent() {
        return false;
    }

    @Override
    public SourceData buildWithoutWrapper(File target) {

        return new SourceData(target.toPath(),false,Type.GRADLE);
    }
}
