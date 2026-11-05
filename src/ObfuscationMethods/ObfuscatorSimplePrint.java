package ObfuscationMethods;

import InjectionMethods.InjectionMethodInterface;
import JarTools.JarData;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.function.Consumer;
import java.util.jar.JarEntry;

public class ObfuscatorSimplePrint implements ObfuscatorInterface<Consumer<MethodVisitor>> {

    @Override
    public Consumer<MethodVisitor> generatePayload() {
        return mv->{
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("https://dsv.su.se");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V", false);
        };
    }

    InjectionMethodInterface injectionMethod;

    public ObfuscatorSimplePrint(InjectionMethodInterface method){
        this.injectionMethod=method;
    }

    @Override
    public InjectionMethodInterface method() {
        return this.injectionMethod;
    }

    @Override
    public boolean isApplicable(JarEntry target, JarData targetData) {
        return injectionMethod.isConditionTrue(target,targetData);
    }

    @Override
    public void reset() {
        this.injectionMethod.reset();
    }

    @Override
    public void initiate(){
        this.injectionMethod.initiate();
    }

}
