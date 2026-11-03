package BytecodeIteration;

import DetectionTools.DetectionToolBytecode;
import org.objectweb.asm.MethodVisitor;

public class DetectionMethodTraveller extends MethodVisitor {
    DetectionToolBytecode bt;

    protected DetectionMethodTraveller(int api, MethodVisitor methodVisitor, DetectionToolBytecode bt) {
        super(api, methodVisitor);
        this.bt=bt;
    }

}
