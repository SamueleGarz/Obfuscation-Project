package BytecodeIteration.Injection;

import JarTools.InjectionData;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GenerateDataClassTraveller extends ClassVisitor {

    InjectionData data;
    String name;

    public GenerateDataClassTraveller(int api, InjectionData data, String filePath) {
        super(api);
        this.data=data;
        this.name=filePath;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible){
        if(descriptor.equals("Lnet/minecraftforge/fml/common/Mod;") || descriptor.equals("Lnet/neoforged/fml/common/Mod;")){
            data.main.add(name);
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        return new GenerateDataMethodTraveller(Opcodes.ASM9,data,this.name);
    }

}
