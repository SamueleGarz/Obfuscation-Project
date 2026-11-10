package InjectionMethods;

import JarTools.InjectionData;

import java.util.jar.JarEntry;

public class InjectionMethodEverywhere implements InjectionMethodInterface {

    @Override
    public boolean isConditionTrue(JarEntry entry, InjectionData data) {
        return true;
    }

    @Override
    public void reset(){}

    @Override
    public void initiate(){}
}
