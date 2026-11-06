package JarTools;

import Main.Helper;
import BuildTools.PluginType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JarData {
    public String main;
    PluginType type;
    public Path filePath;
    List<JarEntryDetectionData> entryList=new ArrayList<>();

    JarData(){
        this.main="";
        type=null;
        this.filePath=null;
    }

    JarData(String mainClass, Path filePath){
        this.main=mainClass;
        this.filePath=filePath;
    }

    public boolean addEntry(JarEntryDetectionData data){
        if(entryList==null || data==null) return false;
        try{
            entryList.add(data);
            return true;
        }catch (Exception e){
            Helper.printRed("Error (JarFile): Unable to add to the payload list the payload: "+data.entryName+": "+e);
        }
        return false;
    }

    public void printAllEntries(String prefix){
        for(JarEntryDetectionData a: entryList){
            Helper.printResultCyan(prefix+"Entry",a.entryName);
            Helper.print(a.payloadsToJson().replaceAll("\\{+",prefix+"\t").replaceAll("\\}+,?","\n"));
        }
    }

    public String getJson(){
        String ret="{\"Main\":\""+main+"\",\"Type\":\""+type+"\",\"filePath\":\""+filePath+"\",\"entryList\":\"{";
        for(JarEntryDetectionData a: entryList){
            ret=ret.concat(a.payloadsToJson());
        }
        return ret.concat("}}");
    }

    public void printNonEmpty(String prefix){
        for(JarEntryDetectionData a: entryList){
            if(!a.payloadFound.isEmpty()){
                Helper.printResultCyan(prefix+"Entry",a.entryName);
//                Helper.print(Helper.replaceAndHighlight(Helper.replaceAndHighlight(Helper.replaceAndHighlight(a.payloadsToJson().replaceAll("\\{+",prefix+"\t").replaceAll("\\}+,?","\n").stripTrailing(),"\"value\"", Helper.TextColors.YELLOW),"\"location\"", Helper.TextColors.PURPLE),"\"type\"", Helper.TextColors.BLUE));;
                Helper.print(Helper.replaceAndHighlight(a.payloadsToJson().replaceAll("\\{+",prefix+"\t").replaceAll("\\}+,?","\n").stripTrailing(), new String[]{"\"location\"", "\"value\"","\"decodesTo\"","\"type\""},new Helper.TextColors[]{Helper.TextColors.PURPLE, Helper.TextColors.YELLOW, Helper.TextColors.GREEN, Helper.TextColors.BLUE}));
            }
        }
    }

}
