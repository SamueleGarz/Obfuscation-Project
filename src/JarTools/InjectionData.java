package JarTools;

import BuildTools.PluginType;
import Main.Helper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InjectionData extends JarDataAbstract{
    public String main;
    PluginType type;
    public Path filePath;
    List<JarEntryData> entryList=new ArrayList<>();

    InjectionData(){
        this.main="";
        type=PluginType.OTHER;
        this.filePath=null;
    }

    InjectionData(String mainClass, Path filePath){
        this.main=mainClass;
        this.filePath=filePath;
    }

    public String getJson(){
        return "{\"Main\":\""+main+"\",\"Type\":\""+type+"\",\"filePath\":\""+filePath+"}";
    }

    @Override
    public void printNonEmpty(String prefix) {
        Helper.print(Helper.replaceAndHighlight(
                prefix+"- "+this.filePath.toString()+"\n"+prefix+"\tMain: "+this.main+"\n"+prefix+"\tType: "+this.type,
                new String[]{"- "+this.filePath.toString(),"Main: "+this.main,"Type: "+this.type},
                new Helper.TextColors[]{Helper.TextColors.PURPLE_BG,Helper.TextColors.CYAN}));
        super.printNonEmpty(prefix);
    }

    @Override
    public void printAllEntries(String prefix){
        Helper.print(Helper.replaceAndHighlight(
                prefix+"- "+this.filePath.toString()+"\n"+prefix+"\tMain: "+this.main+"\n"+prefix+"\tType: "+this.type,
                new String[]{"- "+this.filePath.toString(),"Main:","Type:"},
                new Helper.TextColors[]{Helper.TextColors.PURPLE_BG,Helper.TextColors.CYAN}));
        super.printAllEntries(prefix);
    }


}
