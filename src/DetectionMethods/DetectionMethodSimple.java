package DetectionMethods;

import BytecodeIteration.DetectionClassTravellerAbstract;
import JarTools.JarData;
import Main.Helper;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DetectionMethodSimple extends DetectionMethodBytecodeAbstract {

    public DetectionMethodSimple(InitiateClassTraveller ict, DetectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller) {
        super(ict, methodTraveller);
    }

    @Override
    public void detect(JarFile jar, JarData data) {
        Enumeration<JarEntry> entries= jar.entries();
        while(entries.hasMoreElements()){
            JarEntry entry=entries.nextElement();
            if(entry.getRealName().endsWith(".class")){
                try(InputStream classStream= jar.getInputStream(entry)){
                    byte[] classBytes = classStream.readAllBytes();
                    data.addEntry(explore(classBytes,entry.getRealName()));
                }catch (Exception e){
                    Helper.printRed("\tError (Detection): Exception arose on the inputStream of "+entry.getRealName()+":\n"+e);
                }
            }
        }
    }
}
