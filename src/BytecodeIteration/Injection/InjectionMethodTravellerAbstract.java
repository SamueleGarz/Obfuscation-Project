package BytecodeIteration.Injection;

import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionMethodTravellerAbstract extends MethodVisitor{
    ///Name of the method being observed
    public final String methodName;

    ///Payloads to inject somewhere in the bytecode
    public final List<Consumer<MethodVisitor>> payloads;


    protected InjectionMethodTravellerAbstract(int api, MethodVisitor methodVisitor, String methodName, List<Consumer<MethodVisitor>> payloads) {
        super(api, methodVisitor);
        this.methodName=methodName;
        this.payloads=payloads;
    }
}
