package BytecodeIteration.Detection.MethodTraveller;

import JarTools.JarEntryData;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;

///TODO: this implementation is a little naive in the sense that it's not considering signatures, descriptors, owners, exceptions, certain handler arguments, ecc
public class LengthAndCount extends Abstract{

    //for future works possibly
//    int totalLengthField =0;
//    int fieldCount=0;
//    int totalLengthMethod =0;
//    int methodCount=0;

    HashMap<String,Integer> wordCount=new HashMap<>();

    //thresholds set in accordance with the paper "Detection of Obfuscation in Java Malware" by "Renuka Kumara, Anand Raj Essar Vaishakh"
    private final int LENGTH_THRESHOLD=3;
    private final int REPETITION_THRESHOLD=33;

    public LengthAndCount(int api, MethodVisitor methodVisitor, JarEntryData jarEntryData, String methodName) {
        super(api, methodVisitor, jarEntryData, methodName);
    }

    @Override
    public void visitFieldInsn(int opcode,
                               String owner,
                               String name,
                               String descriptor){
        //this threshold is set in accordance with the paper "Detection of Obfuscation in Java Malware" by "Renuka Kumara, Anand Raj Essar Vaishakh"
        if(!name.isBlank()){
            if(name.length()<LENGTH_THRESHOLD && !wordCount.containsKey(name)){
                this.detectionData.addPayload(methodName,name,"Field_"+owner);
            }
//            fieldCount++;
//            totalLengthField +=name.length();
            wordCount.merge(name,1,Integer::sum);
        }
        super.visitFieldInsn(opcode,owner,name,descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode,
                                String owner,
                                String name,
                                String descriptor,
                                boolean isInterface){
        if(!name.isBlank()){
            if(name.length()<LENGTH_THRESHOLD && !wordCount.containsKey(name)){
                this.detectionData.addPayload(methodName,name,"Method_"+owner);

            }
//            methodCount++;
//            totalLengthMethod +=name.length();
            wordCount.merge(name,1,Integer::sum);
        }
        super.visitMethodInsn(opcode,owner,name,descriptor,isInterface);
    }

    ///TODO: parse the bootstrap method arguments?
    @Override
    public void visitInvokeDynamicInsn(String name,
                                       String descriptor,
                                       Handle bootstrapMethodHandle,
                                       Object... bootstrapMethodArguments){
        if(!name.isBlank()){
            if(name.length()<LENGTH_THRESHOLD && !wordCount.containsKey(name)){
                this.detectionData.addPayload(methodName,name,"Dynamic");

            }
//            methodCount++;
//            totalLengthMethod +=name.length();
            wordCount.merge(name,1,Integer::sum);
        }
        String handleName=bootstrapMethodHandle.getName();
        if(!handleName.isBlank()){
            if(handleName.length()<LENGTH_THRESHOLD && !wordCount.containsKey(name)){
                this.detectionData.addPayload(methodName,name,"Handle");

            }
//            methodCount++;
//            totalLengthMethod +=handleName.length();
            wordCount.merge(handleName,1,Integer::sum);
        }
        super.visitInvokeDynamicInsn(name,descriptor,bootstrapMethodHandle,bootstrapMethodArguments);
    }

    @Override
    public void visitLdcInsn(Object value){
        if(value instanceof Handle){
            String name=((Handle) value).getName();
            if(!name.isBlank()){
                if(name.length()<LENGTH_THRESHOLD && !wordCount.containsKey(name)){
                    this.detectionData.addPayload(methodName,name,"HandleLdc");
                }
//                methodCount++;
//                totalLengthMethod +=name.length();
                wordCount.merge(name,1,Integer::sum);
            }
        }
        super.visitLdcInsn(value);
    }

    //the paper I am basing this on technically does not include local variables, therefore it should be excluded
//    @Override
//    public void visitLocalVariable(String name,
//                                   String descriptor,
//                                   String signature,
//                                   Label start,
//                                   Label end,
//                                   int index) {
//        if (!name.isBlank()) {
//            if (name.length() < LENGTH_THRESHOLD && !wordCount.containsKey(name)) {
//                this.detectionData.addPayload(methodName,name,"LocalVariable");
//
//            }
////            fieldCount++;
////            totalLengthField += name.length();
//            wordCount.merge(name, 1, Integer::sum);
//        }
//        super.visitLocalVariable(name,descriptor,signature,start,end,index);
//    }

    @Override
    public void visitEnd(){
        String pd="resultsMethodLength";
        boolean worthAdding=false;
        if(!wordCount.isEmpty()){
            for(String key: wordCount.keySet()){
                if(wordCount.get(key)>REPETITION_THRESHOLD){
                    worthAdding=true;
                    pd=pd.concat("\",\""+key+"\":\""+wordCount.get(key).toString());
                }
            }
        }
        if(worthAdding){
            this.detectionData.addPayload(methodName,pd,"Result");
        }
        super.visitEnd();
    }

}
