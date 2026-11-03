package InjectionMethods;

import JarTools.JarData;

import java.util.jar.JarEntry;

public class InjectionMethodEverywhere implements InjectionMethodInterface {

    @Override
    public boolean isConditionTrue(JarEntry entry, JarData data) {
        return true;
    }

    @Override
    public void reset(){}
}
