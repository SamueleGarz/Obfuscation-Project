package BytecodeIteration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionClassTraveller extends ClassVisitor {

    List<Consumer<MethodVisitor>> payloads=null;
    public InjectionClassTraveller(int api, ClassVisitor classVisitor, List<Consumer<MethodVisitor>> payloads) {
        super(api, classVisitor);
        this.payloads=payloads;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        return new InjectionMethodTraveller(this.api,base,payloads);
    }
}
