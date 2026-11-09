package ObfuscationManager;

import JarTools.InjectionData;

import java.util.jar.JarFile;

public interface ObfuscatorManagerInterface {
    /// Pass a jarEntry to iterate and inject based on some criteria
    /// @param jar The JarFile to target for inject
    /// @param data The InjectionData to take data for injection from
    void inject(JarFile jar, InjectionData data);

}
