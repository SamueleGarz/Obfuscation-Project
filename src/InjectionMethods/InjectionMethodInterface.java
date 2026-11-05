package InjectionMethods;

import JarTools.JarData;

import java.util.jar.JarEntry;

///Mainly used in the InjectionMethods Enum as an interface to reference
public interface InjectionMethodInterface {

    ///Based on the data in the parameters, determine if the conditions are met for injection
    /// @param entry the JarEntry that is currently being visited
    /// @param data the JarTools.JarData of the Jar being currently visited
    /// @return True if applicable
    boolean isConditionTrue(JarEntry entry, JarData data);

    ///Used to reset any parameter that might have been used previously, such as counters
    void reset();

    ///Used to reset/set up a method before a new file is looked at.
    void initiate();
}
