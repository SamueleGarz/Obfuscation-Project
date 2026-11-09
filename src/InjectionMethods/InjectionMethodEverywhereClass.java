package InjectionMethods;

import JarTools.InjectionData;

import java.util.jar.JarEntry;

public class InjectionMethodEverywhereClass implements InjectionMethodInterface {

    @Override
    public boolean isConditionTrue(JarEntry entry, InjectionData data) {
        return entry.getName().endsWith(".class");
    }

    @Override
    public void reset(){}

    @Override
    public void initiate(){}

}
