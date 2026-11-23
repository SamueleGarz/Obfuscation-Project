package BytecodeIteration.Detection.ClassTraveller;

import JarTools.JarEntryData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class Simple extends Abstract {

    public Simple(int api, ClassVisitor classVisitor, JarEntryData dd, InitiateMethodTraveller methodTraveller) {
        super(api, classVisitor, dd, methodTraveller);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        return methodTraveller.create(this.api,base,this.detectionData, name+"-"+descriptor);
    }
}
