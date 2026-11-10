package BytecodeIteration.Injection;

import JarTools.JarEntryData;
import Main.Helper;
import PayloadGenerators.PayloadData;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.function.Consumer;

public class InjectionMethodTravellerSimple extends InjectionMethodTravellerAbstract{

    public InjectionMethodTravellerSimple(int api, MethodVisitor methodVisitor, String name, List<PayloadData<Consumer<MethodVisitor>>> consumers, JarEntryData data) {
        super(api,methodVisitor,name,consumers,data);
    }

    @Override
    public void visitCode(){
        super.visitCode();
        if(payloads==null){
            return;
        }
        try{
            if (!payloads.isEmpty()){
                String value="";
                for(PayloadData<Consumer<MethodVisitor>> data:payloads){
                    value+=data.value+";";
                    for (Consumer<MethodVisitor> payload: data.payload){
                        payload.accept(this);
                    }
                }
                injectionData.addPayload(methodName,value,"visitCode");
            }
        }catch (Exception e){
            Helper.printRed("Exception arose while injecting payload into"+methodName+":\n"+e);
        }
    }

}
