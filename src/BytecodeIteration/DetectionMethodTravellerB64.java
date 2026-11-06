package BytecodeIteration;

import JarTools.JarEntryDetectionData;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

import java.util.Base64;

///Method traveler class that checks for base64 encrypted strings
public class DetectionMethodTravellerB64 extends DetectionMethodTravellerAbstract {

    ///Check if a string is actually b64
    private boolean isB64(String value){
        //TODO: add a way to check if a string is b64
        boolean looksBase64 = value.matches("^[A-Za-z0-9+/]*={0,2}$") && value.length() % 4 == 0;
        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            String decodedString=new String(decoded);
            return decodedString.chars().allMatch(c -> c >= 0x20 && c <= 0x7E);
        } catch (Exception e) {
            return false;
        }
    }
    public DetectionMethodTravellerB64(int api, MethodVisitor methodVisitor, JarEntryDetectionData dd, String methodName) {
        super(api, methodVisitor, dd, methodName);
    }

    @Override
    public void visitLdcInsn(Object value){
        if(value instanceof String){
            if ((isB64((String) value))){
                this.detectionData.addPayload(methodName, (String)value+",\"decodesTo\":\""+ new String(Base64.getDecoder().decode((String)value))+"\"", "Ldc");
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
                    this.detectionData.addPayload(methodName, (String)value+",\"decodesTo\":\""+ new String(Base64.getDecoder().decode((String)value))+"\"", "invokedynamic");
                }
            }
        }
        super.visitInvokeDynamicInsn(name,descriptor,bootstrapMethodHandle,bootstrapMethodArguments);
    }
}
