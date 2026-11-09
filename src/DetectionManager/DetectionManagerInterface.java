package DetectionManager;

import JarTools.DetectionData;
import JarTools.InjectionData;

import java.util.jar.JarFile;

public interface DetectionManagerInterface {

    /// Pass a jarEntry to iterate and analyze based on some criteria
    /// @param jar The JarFile to target for detection
    /// @param data The InjectionData to which the results will be added in the entries
    void detect(JarFile jar, DetectionData data);
}
