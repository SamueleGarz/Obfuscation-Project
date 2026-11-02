import java.io.File;
import java.util.List;

///Interface for all the classes that will be used to implement a build tool for plugins.
/// Preferably, each implementation should be named Build\<ToolName\>
public interface BuilderInterface {

    /// Applies the whole chain to the given target
    ///
    /// The pipeline should follow this process: isApplicable-> wrapperExists-> BuildWithWrapper-> isBuildToolPresent-> BuildWithoutWrapper
    ///
    /// @param target the Path of the target directory to work with
    /// @return A SourceData structure, build according to the build result
    SourceData apply(File target);

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
    /// @return A SourceData structure, build according to the build result
    SourceData buildWithWrapper(File target);

    /// Check if the build tool is present in the system
    /// @return A boolean value, true if the tool is present
    boolean isBuildToolPresent();

    /// Build the target with the tool implemented
    /// @param target the Path of the target directory to build
    /// @return A SourceData structure, build according to the build result
    SourceData buildWithoutWrapper(File target);
}
