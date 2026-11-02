import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BuildMaven implements BuilderInterface{
    ///Maven presence indicators
    public static final List<String> INDICATOR_FILEs =List.of("pom.xml");

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
        return INDICATOR_FILEs.stream().anyMatch(n -> Files.exists(Paths.get(target + "/" + n)));
    }

    @Override
    public boolean wrapperExists(File target) {
        if(target==null || !target.isDirectory() || !target.canRead()) return false;
        return Files.exists(Paths.get(target.getPath().concat("/mvn")));
    }

    @Override
    public SourceData buildWithWrapper(File target) {
        if (commandExecute.runComm(target,new String[]{"./mvnw","clean","package"}) == 0) {
            return new SourceData(target.toPath(),true,Type.MAVEN);
        }else{
            return new SourceData(target.toPath(),false,Type.MAVEN);
        }
    }

    @Override
    public boolean isBuildToolPresent() {
        return false;
    }

    @Override
    public SourceData buildWithoutWrapper(File target) {
        return new SourceData(target.toPath(),false,Type.MAVEN);
    }
}
