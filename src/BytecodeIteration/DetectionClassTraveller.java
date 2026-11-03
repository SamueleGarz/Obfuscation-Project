package BytecodeIteration;

import DetectionTools.DetectionToolBytecode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class DetectionClassTraveller extends ClassVisitor{
    DetectionToolBytecode bt;

    public DetectionClassTraveller(int api, ClassVisitor classVisitor, DetectionToolBytecode bt) {
        super(api, classVisitor);
        this.bt=bt;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        return new DetectionMethodTraveller(this.api,base,bt);
    }
}
