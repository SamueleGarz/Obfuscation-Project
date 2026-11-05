package JarTools;

import BytecodeIteration.DetectionClassTravellerSimple;
import BytecodeIteration.DetectionMethodTravellerB64;
import DetectionMethods.DetectionMethodBytecodeAbstract;
import DetectionMethods.DetectionMethodInterface;
import DetectionMethods.DetectionMethodSimple;
import Main.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class JarDetector {

    boolean VERBOSE=false;

    List<DetectionMethodInterface> detectionMethods=new ArrayList<>();

    public JarDetector(){
        detectionMethods.add(
                new DetectionMethodSimple(DetectionClassTravellerSimple::new, DetectionMethodTravellerB64::new
                ));
    }

    /// Set the variable VERBOSE in the object
    /// @param verbose true or false boolean value
    public void setVERBOSE(boolean verbose){
        this.VERBOSE=verbose;
    }

    ///
    public void jarExplorer(JarData data){
        try(JarFile jar=new JarFile(data.filePath.toString())){
            for(DetectionMethodInterface detectionMethodInterface:detectionMethods){
                detectionMethodInterface.detect(jar,data);
            }
        }catch (Exception e){
            Helper.printRed("\tError (Detection): Exception arose while opening the jarFile with Path "+data.filePath.toString()+": \n"+e);
        }
    }
}
