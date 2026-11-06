package InjectionMethods;

import JarTools.JarData;

import java.util.jar.JarEntry;

public class InjectionMethodFirstMain implements InjectionMethodInterface {
    private int helperCounter=0;

    @Override
    public boolean isConditionTrue(JarEntry entry, JarData data) {
        if(helperCounter!=0) return false;

        if(!entry.getRealName().equals(data.main)){
            //If the data.main was not found, need a fallback method (turns into an inject once into first class method
            if(data.main.isBlank()){
                this.helperCounter=1;
                return true;
            }
            return false;
        }
        this.helperCounter=1;
        return true;
    }

    @Override
    public void reset(){}

    @Override
    public void initiate(){
        this.helperCounter=0;
    }
}
