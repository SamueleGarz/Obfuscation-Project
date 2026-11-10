package JarTools;

import Main.Helper;

import java.util.ArrayList;
import java.util.List;

public class JarEntryData {
    String entryName;

    private record payloadData(String location, String value, String type) {

        public String returnValues() {
                return "{\"location\":\"" + location + "\", \"value\":\"" + value + "\", \"type\":\"" + type + "\"}";
            }
        }
    List<payloadData> payload =new ArrayList<>();

    public boolean addPayload(String location, String value, String type){
        if(payload ==null || location.isBlank() || value.isBlank() || type.isBlank()) return false;
        try{
            payload.add(new payloadData(location, value, type));
            return true;
        }catch (Exception e){
            Helper.printRed("Error (JarFile): Unable to add to the payload list the payload from: "+location+": "+e);
        }
        return false;
    }

    public JarEntryData(){
        entryName ="";
        payload =null;
    }

    public JarEntryData(String name){
        this.entryName=name;
    }

    public String payloadsToJson(){
        String ret="{";
        for(payloadData a: payload){
            ret=ret.concat(a.returnValues()+",");
        }
        return ret.concat("}");
    }
}
