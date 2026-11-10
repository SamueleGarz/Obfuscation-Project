package JarTools;

import Main.Helper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DetectionData extends JarDataAbstract{
    public Path filePath;

    DetectionData(){
        this.filePath=null;
    }

    public DetectionData(Path filePath){
        this.filePath=filePath;
    }

    public String getJson(){
        String ret="{\"filePath\":\""+filePath+"\",\"entryList\":\"{";
        for(JarEntryData a: entryList){
            ret=ret.concat(a.payloadsToJson());
        }
        return ret.concat("}}");
    }

    @Override
    public void printNonEmpty(String prefix) {
        Helper.print(Helper.replaceAndHighlight(prefix+"- "+this.filePath.toString(),new String[]{"- "+this.filePath.toString()},new Helper.TextColors[]{Helper.TextColors.PURPLE_BG}));
        super.printNonEmpty(prefix);
    }

        @Override
    public void printAllEntries(String prefix){
        Helper.print(Helper.replaceAndHighlight(prefix+"- "+this.filePath.toString(),new String[]{"- "+this.filePath.toString()},new Helper.TextColors[]{Helper.TextColors.PURPLE_BG}));
        super.printAllEntries(prefix);
    }

}
