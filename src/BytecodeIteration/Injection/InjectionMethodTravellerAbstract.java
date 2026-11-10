package BytecodeIteration.Injection;

import JarTools.JarEntryData;
import PayloadGenerators.PayloadData;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionMethodTravellerAbstract extends MethodVisitor{
    ///Name of the method being observed
    public final String methodName;

    ///Payloads to inject somewhere in the bytecode
    public final List<PayloadData<Consumer<MethodVisitor>>> payloads;

    JarEntryData injectionData;

    protected InjectionMethodTravellerAbstract(int api, MethodVisitor methodVisitor, String methodName, List<PayloadData<Consumer<MethodVisitor>>> payloads, JarEntryData data) {
        super(api, methodVisitor);
        this.methodName=methodName;
        this.payloads=payloads;
        this.injectionData=data;
    }
}
