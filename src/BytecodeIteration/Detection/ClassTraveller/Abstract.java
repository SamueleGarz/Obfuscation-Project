package BytecodeIteration.Detection.ClassTraveller;

import JarTools.JarEntryData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class Abstract extends ClassVisitor {
    ///The structure used to store data during detection
    JarEntryData detectionData;
    InitiateMethodTraveller methodTraveller;

    ///Used to make it so on creation of an instance of the class the constructor of a generic Abstract can be passed
    /// using Abstract::new
    @FunctionalInterface
    public interface InitiateMethodTraveller{
        BytecodeIteration.Detection.MethodTraveller.Abstract create(int api, MethodVisitor methodVisitor, JarEntryData jarEntryData, String methodName);
    }

    protected Abstract(int api, ClassVisitor classVisitor, JarEntryData jarEntryData, InitiateMethodTraveller methodTraveller) {
        super(api, classVisitor);
        this.detectionData= jarEntryData;
        this.methodTraveller=methodTraveller;
    }
}
