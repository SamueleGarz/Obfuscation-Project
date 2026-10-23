import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodTraveller extends MethodVisitor {
    protected MethodTraveller(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitCode(){
        super.visitCode();

        //Add to every method a System.out.println("https://dsv.su.se")
        visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        visitLdcInsn("https://dsv.su.se");
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V", false);
    }

}
