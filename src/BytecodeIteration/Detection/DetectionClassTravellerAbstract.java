package BytecodeIteration.Detection;

import JarTools.JarEntryData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class DetectionClassTravellerAbstract extends ClassVisitor {
    ///The structure used to store data during detection
    JarEntryData detectionData;
    InitiateMethodTraveller methodTraveller;

    ///Used to make it so on creation of an instance of the class the constructor of a generic DetectionMethodTravellerAbstract can be passed
    /// using DetectionMethodTravellerAbstract::new
    @FunctionalInterface
    public interface InitiateMethodTraveller{
        DetectionMethodTravellerAbstract create(int api, MethodVisitor methodVisitor, JarEntryData jarEntryData, String methodName);
    }

    protected DetectionClassTravellerAbstract(int api, ClassVisitor classVisitor, JarEntryData jarEntryData, InitiateMethodTraveller methodTraveller) {
        super(api, classVisitor);
        this.detectionData= jarEntryData;
        this.methodTraveller=methodTraveller;
    }
}
