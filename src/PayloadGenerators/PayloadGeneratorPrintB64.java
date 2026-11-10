package PayloadGenerators;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

public class PayloadGeneratorPrintB64 implements PayloadGeneratorInterface<Consumer<MethodVisitor>>{
    /// Produce a payload that consists of the instructions required to add in the bytecode stack the operation to produce System.out.println(Base64.decode(base))
    /// @param base The base string to be used for the payload. It's actual use can depend based on implementation
    /// @return the list of methods to be consumed by a methodTraveller to add the instructions to the stack
    @Override
    public PayloadData<Consumer<MethodVisitor>> producePayload(String base) {
        String encoded = Base64.getEncoder().encodeToString(base.getBytes(StandardCharsets.UTF_8));

        return new PayloadData<Consumer<MethodVisitor>>(
                List.of(mv-> {
                    //push to the stack the System.out (printstream is the ned point where println goes to) so I can build on top of it to get the final result
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    //Allocates a new string and push the reference to the stack (not yet inititated)
                    mv.visitTypeInsn(Opcodes.NEW, "java/lang/String");
                    //since the constructur consumes one instance on the stack, the dup makes a second one to be used
                    mv.visitInsn(Opcodes.DUP);
                    // pushes a Base64.getDecoder() to the stack
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Base64", "getDecoder",
                            "()Ljava/util/Base64$Decoder;", false);
                    //push the string encoded (as a constant)
                    mv.visitLdcInsn(encoded);
                    //uses the string in the stack as an argument to be passed to the decoder on the stack and pushes the result on the stack
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Base64$Decoder", "decode",
                            "(Ljava/lang/String;)[B", false);
                    //string is now initiated using the result from the decoding on the stack and the previously placed duplicate reference
                    //once this is done, the previous reference that was duped will now be to this new string (the method returns nothing, that's why
                    //it needed to be duplicated on the stack
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/String", "<init>", "([B)V", false);
                    //consumes the instruction pushed at the beginning and the now new string made from the encoding process
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                            "(Ljava/lang/String;)V", false);
                }), encoded);
    }
}
