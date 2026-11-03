import java.io.File;

///Enum with all the currently implemented tool
enum BuildType {
    GRADLE("build/libs"),
    MAVEN("mavenBuildPath"),
    OTHER("");

    public final String buildDir;

    private BuildType(String buildDir) {
        this.buildDir = buildDir;
    }
}
///Interface for all the classes that will be used to implement a build tool for plugins.
///
/// Preferably, each implementation should be named Build\<ToolName\>
///
/// ***Once a new tool is added, the name in caps should be added to the enum BuildType***
public interface BuilderInterface {

    /// Applies the whole chain to the given target
    ///
    /// The pipeline should follow this process: isApplicable-> wrapperExists-> BuildWithWrapper-> isBuildToolPresent-> BuildWithoutWrapper
    ///
    /// @param target the Path of the target directory to work with
    /// @return A BuildData structure, build according to the build result
    BuildData apply(File target);

    /// Check if the target can be built using the tool implemented by the class
    /// @param target the Path of the target directory to check for applicability
    /// @return A boolean value, true if the tool is applicable
    boolean isApplicable(File target);

    /// Check if the taget contains the wrapper for the tool implemented by the class
    /// @param target the Path of the target directory to check for the presence of the wrapper
    /// @return A boolean value, true when the wrapper exists, false if not or the wrapper does not exist
    boolean wrapperExists(File target);

    /// Build the target using the wrapper of the tool implemented
    /// @param target the Path of the target directory to build with the wrapper
    /// @return A BuildData structure, build according to the build result
    BuildData buildWithWrapper(File target);

    /// Check if the build tool is present in the system
    /// @return A boolean value, true if the tool is present
    boolean isBuildToolPresent();

    /// Build the target with the tool implemented
    /// @param target the Path of the target directory to build
    /// @return A SourceData structure, build according to the build result
    BuildData buildWithoutWrapper(File target);
}
