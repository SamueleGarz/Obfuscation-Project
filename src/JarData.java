public class JarData {
    public String main;
    PluginType type;

    JarData(){
        this.main="";
        type=null;
    }
    JarData(String mainClass){
        this.main=mainClass;
    }

}
