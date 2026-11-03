import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BuildMaven implements BuilderInterface{
    ///Maven presence indicators
    public static final List<String> INDICATOR_FILEs =List.of("pom.xml");

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
        return INDICATOR_FILEs.stream().anyMatch(n -> Files.exists(Paths.get(target + "/" + n)));
    }

    @Override
    public boolean wrapperExists(File target) {
        if(!Helper.isValidDir(target)) return false;
        return Files.exists(Paths.get(target.getPath().concat("/mvn")));
    }

    @Override
    public BuildData buildWithWrapper(File target) {
        if (commandExecute.runComm(target,new String[]{"./mvnw","clean","package"}) == 0) {
            return new BuildData(target.toPath(),true, BuildType.MAVEN);
        }else{
            return new BuildData(target.toPath(),false, BuildType.MAVEN);
        }
    }

    @Override
    public boolean isBuildToolPresent() {
        return false;
    }

    @Override
    public BuildData buildWithoutWrapper(File target) {
        return new BuildData(target.toPath(),false, BuildType.MAVEN);
    }
}
