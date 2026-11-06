package BytecodeIteration;

import Main.Helper;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionMethodTraveller extends MethodVisitor {

    List<Consumer<MethodVisitor>> payloads;

    protected InjectionMethodTraveller(int api, MethodVisitor methodVisitor, List<Consumer<MethodVisitor>> payloads) {
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
