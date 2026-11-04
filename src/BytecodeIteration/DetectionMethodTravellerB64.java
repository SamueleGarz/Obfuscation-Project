package BytecodeIteration;

import DetectionMethods.DetectionMethodBytecodeAbstract;
import JarTools.JarEntryDetectionData;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

///Method traveler class that checks for base64 encrypted strings
public class DetectionMethodTravellerB64 extends DetectionMethodTravellerAbstract {

    ///Check if a string is actually b64
    private boolean isB64(String value){
        //TODO: add a way to check if a string is b64
        return value.length()>6;
    }
    public DetectionMethodTravellerB64(int api, MethodVisitor methodVisitor, JarEntryDetectionData dd, String methodName) {
        super(api, methodVisitor, dd, methodName);
    }

    @Override
    public void visitLdcInsn(Object value){
        if(value instanceof String){
            if ((isB64((String) value))){
                this.detectionData.addPayload(methodName, (String)value, "Ldc");
            }
        }
        super.visitLdcInsn(value);
    }

    @Override
    public void visitInvokeDynamicInsn(String name,
                                       String descriptor,
                                       Handle bootstrapMethodHandle,
                                       Object... bootstrapMethodArguments){
        for(Object value:bootstrapMethodArguments){
            if(value instanceof String){
                if ((isB64((String) value))){
                    this.detectionData.addPayload(methodName, (String)value, "invokedynamic");
                }
            }
        }
        super.visitInvokeDynamicInsn(name,descriptor,bootstrapMethodHandle,bootstrapMethodArguments);
    }
}
