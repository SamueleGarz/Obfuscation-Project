package BytecodeIteration.Detection;

import JarTools.JarEntryDetectionData;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

public class DetectionMethodTravellerSimple extends DetectionMethodTravellerAbstract {

    List<String> valuesCleartext=new ArrayList<>();

    protected DetectionMethodTravellerSimple(int api, MethodVisitor methodVisitor, JarEntryDetectionData dd, String methodName) {
        super(api, methodVisitor, dd, methodName);
        valuesCleartext.add(
                "dsv.su.se"
        );
    }

    @Override
    public void visitLdcInsn(Object value){
        if(value instanceof String){
            for(String a: valuesCleartext){
                if (((String) value).contains(a)){
                    this.detectionData.addPayload(methodName, (String)value, "Ldc");
                }
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
                for(String a: valuesCleartext){
                    if (((String) value).contains(a)){
                        this.detectionData.addPayload(methodName, (String)value, "invokedynamic");
                    }
                }
            }
        }
        super.visitInvokeDynamicInsn(name,descriptor,bootstrapMethodHandle,bootstrapMethodArguments);
    }

}
