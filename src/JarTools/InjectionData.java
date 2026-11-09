package JarTools;

import Main.Helper;
import BuildTools.PluginType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InjectionData {
    public String main;
    PluginType type;
    public Path filePath;
    List<JarEntryDetectionData> entryList=new ArrayList<>();

    InjectionData(){
        this.main="";
        type=null;
        this.filePath=null;
    }

    InjectionData(String mainClass, Path filePath){
        this.main=mainClass;
        this.filePath=filePath;
    }

    public String getJson(){
        return "{\"Main\":\""+main+"\",\"Type\":\""+type+"\",\"filePath\":\""+filePath+"}";
    }

}
