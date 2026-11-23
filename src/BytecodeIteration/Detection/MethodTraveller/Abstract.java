package BytecodeIteration.Detection.MethodTraveller;

import JarTools.JarEntryData;
import org.objectweb.asm.MethodVisitor;

public abstract class Abstract extends MethodVisitor {

    ///The structure used to store data during detection
    JarEntryData detectionData;

    ///Name of the method being observed
    String methodName;

    protected Abstract(int api, MethodVisitor methodVisitor, JarEntryData jarEntryData, String methodName) {
        super(api, methodVisitor);
        this.detectionData= jarEntryData;
        this.methodName=methodName;
    }
}
