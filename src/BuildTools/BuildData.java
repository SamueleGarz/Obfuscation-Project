package BuildTools;

import java.nio.file.Path;

///Class made to store the data resulting from the build process
public class BuildData {
    ///Path to the Directory
    public Path sourcePath;
    ///True if the build was successful
    public boolean builds;
    ///Build tool used for successful build
    public BuildType buildType;

    /// Used to build the object with parameter
    /// @param path Path to the Directory
    /// @param built True if the build was successful
    /// @param buildType Build tool used for successful build, Type.OTHER if not successful / unknown tool
    public BuildData(Path path, Boolean built, BuildType buildType){
        this.sourcePath=path;
        this.builds=built;
        this.buildType= buildType;
    }

    ///Used to build the object with no parameter, by default:
    /// - sourcePath=null
    /// - builds=false
    /// - buildType=Type.OTHER
    public BuildData(){
        this.sourcePath=null;
        this.builds=false;
        this.buildType= BuildType.OTHER;
    }

}
