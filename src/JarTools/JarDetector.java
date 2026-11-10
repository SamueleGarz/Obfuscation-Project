package JarTools;

import BytecodeIteration.Detection.DetectionClassTravellerSimple;
import BytecodeIteration.Detection.DetectionMethodTravellerB64;
import DetectionManager.DetectionManagerInterface;
import DetectionManager.DetectionManagerBytecode;
import Main.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class JarDetector {

    boolean VERBOSE=false;

    List<DetectionManagerInterface> detectionMethods=new ArrayList<>();

    public JarDetector(){
        detectionMethods.add(
                new DetectionManagerBytecode(
                        DetectionClassTravellerSimple::new,
                        DetectionMethodTravellerB64::new
                ));
    }

    /// Set the variable VERBOSE in the object
    /// @param verbose true or false boolean value
    public void setVERBOSE(boolean verbose){
        this.VERBOSE=verbose;
    }

    ///
    public void jarExplorer(DetectionData data){
        try(JarFile jar=new JarFile(data.filePath.toString())){
            for(DetectionManagerInterface detectionManagerInterface :detectionMethods){
                detectionManagerInterface.detect(jar,data);
            }
        }catch (Exception e){
            Helper.printRed("\tError (Detection): Exception arose while opening the jarFile with Path "+data.filePath.toString()+": \n"+e);
        }
    }
}
