package DetectionTools;

import BytecodeIteration.DetectionClassTraveller;
import JarTools.JarData;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.util.jar.JarEntry;

import static org.objectweb.asm.Opcodes.ASM9;

public abstract class DetectionToolBytecode implements DetectionToolInterface{

    DetectionData result=new DetectionData();

    public abstract DetectionData detect(JarEntry target, JarData data);

    public DetectionData explore(JarEntry target, JarData data, byte[] classBytes){
        ClassReader cr=new ClassReader(classBytes);
        ClassWriter cw=new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        DetectionClassTraveller ct=new DetectionClassTraveller(ASM9,cw,this);
        return result;
    }


}
