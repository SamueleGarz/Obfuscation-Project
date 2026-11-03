package InjectionMethods;

import JarTools.JarData;

import java.util.jar.JarEntry;

public class InjectionMethodEverywhereClass implements InjectionMethodInterface {

    @Override
    public boolean isConditionTrue(JarEntry entry, JarData data) {
        return entry.getName().endsWith(".class");
    }

    @Override
    public void reset(){}
}
