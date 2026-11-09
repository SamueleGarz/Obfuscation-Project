package BytecodeIteration.Injection;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionClassTravellerSimple extends InjectionClassTravellerAbstract{
    public InjectionClassTravellerSimple(int api, ClassVisitor classVisitor, List<Consumer<MethodVisitor>> payloads, InitiateMethodTraveller methodTraveller) {
        super(api, classVisitor, payloads, methodTraveller);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        return methodTraveller.create(this.api, base, name+"-"+descriptor, this.payloads);
    }
}
