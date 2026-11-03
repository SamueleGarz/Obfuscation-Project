package DetectionTools;

import java.util.jar.JarEntry;

public interface DetectionToolInterface {

    /// Pass a jarEntry to iterate and analyze based on some criteria
    /// @param target The jarEntry to target for detection
    /// @return
    DetectionData detect(JarEntry target, JarTools.JarData data);
}
