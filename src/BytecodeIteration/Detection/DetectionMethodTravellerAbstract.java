package BytecodeIteration.Detection;

import JarTools.JarEntryData;
import org.objectweb.asm.MethodVisitor;

public abstract class DetectionMethodTravellerAbstract extends MethodVisitor {

    ///The structure used to store data during detection
    JarEntryData detectionData;

    ///Name of the method being observed
    String methodName;

    protected DetectionMethodTravellerAbstract(int api, MethodVisitor methodVisitor, JarEntryData jarEntryData, String methodName) {
        super(api, methodVisitor);
        this.detectionData= jarEntryData;
        this.methodName=methodName;
    }
}
