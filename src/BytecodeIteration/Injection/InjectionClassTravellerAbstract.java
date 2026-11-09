package BytecodeIteration.Injection;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionClassTravellerAbstract extends ClassVisitor{

    ///Used to create a InjectionMethodTravellerAbstract
    InitiateMethodTraveller methodTraveller;

    ///Payloads to inject somewhere in the bytecode
    List<Consumer<MethodVisitor>> payloads;

    /// Used to make it so on creation of an instance of the class the constructor of a generic InjectionMethodTravellerAbstract can be passed
    /// using InjectionMethodTravellerAbstract::new
    @FunctionalInterface
    public interface InitiateMethodTraveller{
        InjectionMethodTravellerAbstract create(int api, MethodVisitor methodVisitor, String methodName, List<Consumer<MethodVisitor>> payloads);
    }

    protected InjectionClassTravellerAbstract(int api, ClassVisitor classVisitor,List<Consumer<MethodVisitor>> payloads, InitiateMethodTraveller methodTraveller) {
        super(api, classVisitor);
        this.methodTraveller=methodTraveller;
        this.payloads=payloads;
    }
}
