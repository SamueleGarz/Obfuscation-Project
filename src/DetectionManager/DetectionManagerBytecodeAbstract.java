package DetectionManager;

import BytecodeIteration.Detection.ClassTraveller.Abstract;
import JarTools.JarEntryData;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM9;

public abstract class DetectionManagerBytecodeAbstract implements DetectionManagerInterface {

    ///Used to make it so on creation of an instance of the class the constructor of a generic Abstract can be passed
    /// using Abstract::new
    @FunctionalInterface
    public interface InitiateClassTraveller{
        Abstract create(int api, ClassVisitor classVisitor, JarEntryData dd, Abstract.InitiateMethodTraveller methodTraveller);
    }

    ///used to initialize the class traveller to be used
    private final InitiateClassTraveller initiateClassTraveller;
    private final Abstract.InitiateMethodTraveller methodTraveller;
    private final String identifier;

    DetectionManagerBytecodeAbstract(InitiateClassTraveller ict, Abstract.InitiateMethodTraveller methodTraveller, String identifier){
        this.initiateClassTraveller=ict;
        this.methodTraveller=methodTraveller;
        this.identifier=identifier;
    }

    ///Explore the given classBytes using and ASM Class Reader and a Abstract
    public JarEntryData explore(byte[] classBytes, String entryName){

        JarEntryData result=new JarEntryData(entryName+identifier);
        ClassReader cr=new ClassReader(classBytes);
        ClassWriter cw=new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        Abstract detectionClassTraveller=this.initiateClassTraveller.create(ASM9,cw,result,methodTraveller);
        cr.accept(detectionClassTraveller,ClassReader.EXPAND_FRAMES);

        return result;
    }

}
