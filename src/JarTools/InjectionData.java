package JarTools;

import BuildTools.PluginType;
import Main.Helper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InjectionData extends JarDataAbstract{
    public List<String> main=new ArrayList<>();
    PluginType type;
    public Path filePath;
    List<JarEntryData> entryList=new ArrayList<>();
    public List<String> dependencies=new ArrayList<>();
    public Path source;

    InjectionData(){
        type=PluginType.OTHER;
        this.filePath=null;
    }

    InjectionData(String mainClass, Path filePath){
        this.main.add(mainClass);
        this.filePath=filePath;
    }

    public String getJson(){
        return "{\"Main\":\""+main.toString().replace("[","").replace("]","")+"\",\"Type\":\""+type+"\",\"filePath\":\""+filePath+"}";
    }

    @Override
    public void printNonEmpty(String prefix) {
        Helper.print(Helper.replaceAndHighlight(
                prefix+"- "+this.filePath.toString()+"\n"+prefix+"\tMain: "+this.main.toString().replace("[","").replace("]","")+"\n"+prefix+"\tType: "+this.type,
                new String[]{"- "+this.filePath.toString(),"Main: "+this.main.toString().replace("[","").replace("]",""),"Type: "+this.type},
                new Helper.TextColors[]{Helper.TextColors.PURPLE_BG,Helper.TextColors.CYAN}));
        super.printNonEmpty(prefix);
    }

    @Override
    public void printAllEntries(String prefix){
        Helper.print(Helper.replaceAndHighlight(
                prefix+"- "+this.filePath.toString()+"\n"+prefix+"\tMain: "+this.main.toString().replace("[","").replace("]","")+"\n"+prefix+"\tType: "+this.type,
                new String[]{"- "+this.filePath.toString(),"Main:","Type:"},
                new Helper.TextColors[]{Helper.TextColors.PURPLE_BG,Helper.TextColors.CYAN}));
        super.printAllEntries(prefix);
    }


}
