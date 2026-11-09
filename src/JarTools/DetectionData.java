package JarTools;

import Main.Helper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DetectionData {
    public Path filePath;
    List<JarEntryDetectionData> entryList=new ArrayList<>();

    DetectionData(){
        this.filePath=null;
    }

    public DetectionData(Path filePath){
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

    public void printEntry(JarEntryDetectionData a, String prefix){
        Helper.print(Helper.replaceAndHighlight(prefix+"\tEntry "+a.entryName,new String[]{"Entry "+a.entryName},new Helper.TextColors[]{Helper.TextColors.BLUE_BG}));
        Helper.print(Helper.replaceAndHighlight(a.payloadsToJson().replaceAll("\\{+",prefix+"\t").replaceAll("\\}+,?","\n").stripTrailing(), new String[]{"\"location\"", "\"value\"","\"decodesTo\"","\"type\""},new Helper.TextColors[]{Helper.TextColors.PURPLE, Helper.TextColors.YELLOW, Helper.TextColors.GREEN, Helper.TextColors.BLUE}));
    }

    public void printAllEntries(String prefix){
        for(JarEntryDetectionData a: entryList){
            printEntry(a,prefix);
        }
    }

    public String getJson(){
        String ret="{\"filePath\":\""+filePath+"\",\"entryList\":\"{";
        for(JarEntryDetectionData a: entryList){
            ret=ret.concat(a.payloadsToJson());
        }
        return ret.concat("}}");
    }

    public void printNonEmpty(String prefix){
        Helper.print(Helper.replaceAndHighlight(prefix+"- "+this.filePath.toString(),new String[]{"- "+this.filePath.toString()},new Helper.TextColors[]{Helper.TextColors.PURPLE_BG}));
        for(JarEntryDetectionData a: entryList){
            if(!a.payloadFound.isEmpty()){
                printEntry(a,prefix);
            }
        }
    }

}
