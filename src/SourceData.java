import java.nio.file.Path;

enum Type{
    GRADLE,
    MAVEN,
    OTHER
}

public class SourceData {
    public Path sourcePath;
    public boolean builds;
    Type buildType;

    public String buildDir(){
        switch (this.buildType){
            case GRADLE -> {
                return "build/libs";
            }
            case MAVEN -> {
                return "mavenBuildPath";
            }
            default -> {
                return "";
            }
        }
    }

    SourceData(Path path, Boolean built, Type type){
        this.sourcePath=path;
        this.builds=built;
        this.buildType=type;
    }

    SourceData(){
        this.sourcePath=null;
        this.builds=false;
        this.buildType=Type.OTHER;
    }

}
