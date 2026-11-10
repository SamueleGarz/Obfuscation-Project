package BytecodeIteration.Injection;

import JarTools.JarEntryData;
import PayloadGenerators.PayloadData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionClassTravellerSimple extends InjectionClassTravellerAbstract{
    JarEntryData injectionData;

    public InjectionClassTravellerSimple(int api, ClassVisitor classVisitor, List<PayloadData<Consumer<MethodVisitor>>> payloads, InitiateMethodTraveller methodTraveller, JarEntryData data) {
        super(api, classVisitor, payloads, methodTraveller, data);
        this.injectionData=data;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        return methodTraveller.create(this.api, base, name+"-"+descriptor, this.payloads, injectionData);
    }
}
