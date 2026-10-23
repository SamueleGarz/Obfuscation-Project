import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassTraveller extends ClassVisitor {

    protected ClassTraveller(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        return new MethodTraveller(this.api,base);
    }
}
