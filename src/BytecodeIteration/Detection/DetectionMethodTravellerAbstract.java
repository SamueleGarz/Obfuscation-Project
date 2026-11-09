package BytecodeIteration.Detection;

import JarTools.JarEntryDetectionData;
import org.objectweb.asm.MethodVisitor;

public abstract class DetectionMethodTravellerAbstract extends MethodVisitor {

    ///The structure used to store data during detection
    JarEntryDetectionData detectionData;

    ///Name of the method being observed
    String methodName;

    protected DetectionMethodTravellerAbstract(int api, MethodVisitor methodVisitor, JarEntryDetectionData jarEntryDetectionData, String methodName) {
        super(api, methodVisitor);
        this.detectionData=jarEntryDetectionData;
        this.methodName=methodName;
    }
}
