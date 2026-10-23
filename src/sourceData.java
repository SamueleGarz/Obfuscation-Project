import java.nio.file.Path;

enum Type{
    GRADLE,
    MAVEN,
    OTHER
}

public class sourceData {
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

    sourceData(Path path, Boolean built, Type type){
        this.sourcePath=path;
        this.builds=built;
        this.buildType=type;
    }

    sourceData(){
        this.sourcePath=null;
        this.builds=false;
        this.buildType=Type.OTHER;
    }

}
