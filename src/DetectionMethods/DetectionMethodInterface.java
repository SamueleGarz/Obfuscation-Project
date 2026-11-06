package DetectionMethods;

import java.util.jar.JarFile;

public interface DetectionMethodInterface {

    /// Pass a jarEntry to iterate and analyze based on some criteria
    /// @param jar The JarFile to target for detection
    /// @param data The JarData to which the results will be added in the entries
    void detect(JarFile jar, JarTools.JarData data);
}
