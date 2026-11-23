package InjectionMethods;

import JarTools.InjectionData;

import java.util.jar.JarEntry;

public class InjectionMethodFirstMain implements InjectionMethodInterface {
    private int helperCounter=0;

    @Override
    public boolean isConditionTrue(JarEntry entry, InjectionData data) {
        if(helperCounter>0) return false;
        if(data.main.isEmpty()){
            this.helperCounter=1;
            return true;
        }else {
            for(String val:data.main){
                if(entry.getRealName().equals(val)){
                    this.helperCounter=1;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void reset(){}

    @Override
    public void initiate(){
        this.helperCounter=0;
    }
}
