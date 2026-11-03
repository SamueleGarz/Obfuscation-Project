import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;
import java.util.function.Consumer;

public class MethodTraveller extends MethodVisitor {

    List<Consumer<MethodVisitor>> payloads=null;

    protected MethodTraveller(int api, MethodVisitor methodVisitor, List<Consumer<MethodVisitor>> payloads) {
        super(api, methodVisitor);
        this.payloads=payloads;
    }

    @Override
    public void visitCode(){
        super.visitCode();
        try{
            if (!payloads.isEmpty()){
                for (Consumer<MethodVisitor> payload: payloads){
                    payload.accept(this);
                }
            }
        }catch (Exception e){
            Helper.printRed("Exception arose while injecting payload:\n"+e);
        }
    }

}
