package JarTools;

import Main.PluginType;

import java.nio.file.Path;

public class JarData {
    public String main;
    PluginType type;
    public Path filePath;

    JarData(){
        this.main="";
        type=null;
        this.filePath=null;
    }
    JarData(String mainClass, Path filePath){
        this.main=mainClass;
        this.filePath=filePath;
    }

}
