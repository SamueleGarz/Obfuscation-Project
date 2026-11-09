package ObfuscationManager;

import BytecodeIteration.Detection.DetectionClassTravellerAbstract;
import BytecodeIteration.Injection.InjectionClassTravellerAbstract;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.ASM9;

public abstract class ObfuscatorManagerBytecodeAbstract implements ObfuscatorManagerInterface{
    ///Used to make it so on creation of an instance of the class the constructor of a generic InjectionClassTravellerAbstract can be passed
    /// using InjectionClassTravellerAbstract::new
    @FunctionalInterface
    public interface InitiateClassTraveller{
        InjectionClassTravellerAbstract create(int api, ClassVisitor classVisitor, List<Consumer<MethodVisitor>> payloads, InjectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller);
    }

    ///used to initialize the class traveller to be used
    private final InitiateClassTraveller initiateClassTraveller;
    private final InjectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller;


    /// This class takes a series of generators to produce a payload to be consumed inside a method traveller's insert code.
    /// @param ict the class traveller to be used for the bytecode insertion
    /// @param methodTraveller the method traveller to be used for the bytecode insertion
    ObfuscatorManagerBytecodeAbstract(InitiateClassTraveller ict, InjectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller){
        this.initiateClassTraveller=ict;
        this.methodTraveller=methodTraveller;
    }

    ///Explore the given classBytes using and ASM Class Reader and a InjectionClassTravellerAbstract
    public byte[] explore(byte[] classBytes, List<Consumer<MethodVisitor>> classPayloads){

        ClassReader cr=new ClassReader(classBytes);
        ClassWriter cw=new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        InjectionClassTravellerAbstract injectionClassTraveller=this.initiateClassTraveller.create(ASM9,cw,classPayloads,methodTraveller);
        cr.accept(injectionClassTraveller,ClassReader.EXPAND_FRAMES);

        return cw.toByteArray();
    }

}
