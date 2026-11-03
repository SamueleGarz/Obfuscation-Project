package InjectionMethods;

import JarTools.JarData;

import java.util.jar.JarEntry;

public class InjectionMethodFirstMain implements InjectionMethodInterface {
    int helperCounter=0;
    @Override
    public boolean isConditionTrue(JarEntry entry, JarData data) {
        if(helperCounter!=0 || !entry.getRealName().equals(data.main)) return false;
        helperCounter=1;
        return true;
    }

    @Override
    public void reset(){}
}
