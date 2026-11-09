package BytecodeIteration.Injection;

import Main.Helper;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionMethodTravellerSimple extends InjectionMethodTravellerAbstract{

    public InjectionMethodTravellerSimple(int api, MethodVisitor methodVisitor, String name, List<Consumer<MethodVisitor>> consumers) {
        super(api,methodVisitor,name,consumers);
    }

    @Override
    public void visitCode(){
        super.visitCode();
        if(payloads==null){
            return;
        }
        try{
            if (!payloads.isEmpty()){
                for (Consumer<MethodVisitor> payload: payloads){
                    payload.accept(this);
                }
            }
        }catch (Exception e){
            Helper.printRed("Exception arose while injecting payload into"+methodName+":\n"+e);
        }
    }

}
