package JarTools;

import Main.Helper;

import java.util.ArrayList;
import java.util.List;

public abstract class JarDataAbstract {
    List<JarEntryData> entryList=new ArrayList<>();

    public boolean addEntry(JarEntryData data){
        if(entryList==null || data==null) return false;
        try{
            entryList.add(data);
            return true;
        }catch (Exception e){
            Helper.printRed("Error (JarFile): Unable to add to the payload list the payload: "+data.entryName+": "+e);
        }
        return false;
    }

    public void printEntry(JarEntryData a, String prefix){
        Helper.print(Helper.replaceAndHighlight(prefix+"\tEntry "+a.entryName,new String[]{"Entry "+a.entryName},new Helper.TextColors[]{Helper.TextColors.BLUE_BG}));
        Helper.print(Helper.replaceAndHighlight(a.payloadsToJson().replaceAll("\\{+",prefix+"\t").replaceAll("\\}+,?","\n").stripTrailing(), new String[]{"\"location\"", "\"value\"","\"decodesTo\"","\"type\""},new Helper.TextColors[]{Helper.TextColors.PURPLE, Helper.TextColors.YELLOW, Helper.TextColors.GREEN, Helper.TextColors.BLUE}));
    }

    public void printAllEntries(String prefix){
        for(JarEntryData a: entryList){
            printEntry(a,prefix);
        }
    }

    public void printNonEmpty(String prefix){
        for(JarEntryData a: entryList){
            if(!a.payload.isEmpty()){
                printEntry(a,prefix);
            }
        }
    }
}
