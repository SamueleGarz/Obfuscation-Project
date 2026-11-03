import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.function.Consumer;
import java.util.jar.JarEntry;

///A simple class that will produce a payload for every method inside the class file
public class BytecodeClassObfuscationSimpleMain implements BytecodeClassObfuscationInterface{
    @Override
    public boolean isApplicable(JarEntry target, JarData targetData) {
        return target.getRealName().equals(targetData.main);
    }

    @Override
    public Consumer<MethodVisitor> generatePayload() {
        return mv->{
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("https://dsv.su.se");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V", false);
        };
    }
}
