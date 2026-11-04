package JarTools;

import Main.Helper;

import java.util.ArrayList;
import java.util.List;

public class JarEntryDetectionData {
    String entryName;
    private class payloadData{
        private String location;
        private String value;
        private String type;

        payloadData(String a, String b, String c){
            this.location=a;
            this.value=b;
            this.type=c;
        }

        public String returnValues(){
            return "{\"location\":\""+location+"\", \"value\":\""+value+"\", \"type\":\""+type+"\"}";
        }
    }
    List<payloadData> payloadFound=new ArrayList<>();

    public boolean addPayload(String location, String value, String type){
        if(payloadFound==null || location.isBlank() || value.isBlank() || type.isBlank()) return false;
        try{
            payloadFound.add(new payloadData(location,value,type));
            return true;
        }catch (Exception e){
            Helper.printRed("Error (JarFile): Unable to add to the payload list the payload from: "+location+": "+e);
        }
        return false;
    }

    public JarEntryDetectionData(){
        entryName ="";
        payloadFound=null;
    }

    public JarEntryDetectionData(String name){
        this.entryName=name;
    }

    public String payloadsToJson(){
        String ret="{";
        for(payloadData a: payloadFound){
            ret=ret.concat(a.returnValues()+",");
        }
        return ret.concat("}");
    }
}
