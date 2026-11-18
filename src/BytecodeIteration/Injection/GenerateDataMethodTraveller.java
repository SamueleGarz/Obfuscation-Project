package BytecodeIteration.Injection;

import JarTools.InjectionData;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

public class GenerateDataMethodTraveller extends MethodVisitor {

    InjectionData data;
    final String name;

    protected GenerateDataMethodTraveller(int api, InjectionData data, String name) {
        super(api);
        this.data=data;
        this.name=name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible){
        if(descriptor.equals("Lnet/minecraftforge/fml/common/Mod;") || descriptor.equals("Lnet/neoforged/fml/common/Mod;")){
            data.main.add(name);
        }
        return null;
    }
}
