package DetectionManager;

import BytecodeIteration.Detection.DetectionClassTravellerAbstract;
import JarTools.JarEntryData;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM9;

public abstract class DetectionManagerBytecodeAbstract implements DetectionManagerInterface {

    ///Used to make it so on creation of an instance of the class the constructor of a generic DetectionClassTravellerAbstract can be passed
    /// using DetectionClassTravellerAbstract::new
    @FunctionalInterface
    public interface InitiateClassTraveller{
        DetectionClassTravellerAbstract create(int api, ClassVisitor classVisitor, JarEntryData dd, DetectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller);
    }

    ///used to initialize the class traveller to be used
    private final InitiateClassTraveller initiateClassTraveller;
    private final DetectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller;

    DetectionManagerBytecodeAbstract(InitiateClassTraveller ict, DetectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller){
        this.initiateClassTraveller=ict;
        this.methodTraveller=methodTraveller;
    }

    ///Explore the given classBytes using and ASM Class Reader and a DetectionClassTravellerAbstract
    public JarEntryData explore(byte[] classBytes, String entryName){

        JarEntryData result=new JarEntryData(entryName);
        ClassReader cr=new ClassReader(classBytes);
        ClassWriter cw=new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        DetectionClassTravellerAbstract detectionClassTraveller=this.initiateClassTraveller.create(ASM9,cw,result,methodTraveller);
        cr.accept(detectionClassTraveller,ClassReader.EXPAND_FRAMES);

        return result;
    }

}
