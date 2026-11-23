package BytecodeIteration.Detection.ClassTraveller;

import JarTools.JarEntryData;
import Main.Helper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;

///TODO: add possibly more checks on handles, signatures, descriptors and such
public class LengthAndCount extends Abstract{

    //might be used in the future to add more metrics
//    int totalLengthClass =0;
//    int classCount=0;
//    int totalLengthMethod =0;
//    int methodCount=0;
//    int totalLengthField =0;
//    int fieldCount=0;

    HashMap<String,Integer> wordCount=new HashMap<>();

    //thresholds set in accordance with the paper "Detection of Obfuscation in Java Malware" by "Renuka Kumara, Anand Raj Essar Vaishakh"
    private final int LENGTH_THRESHOLD=3+1;
    private final int REPETITION_THRESHOLD=33;

    public LengthAndCount(int api, ClassVisitor classVisitor, JarEntryData jarEntryData, InitiateMethodTraveller methodTraveller) {
        super(api, classVisitor, jarEntryData, methodTraveller);
    }

    @Override
    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces){
        if (!name.isBlank()) {
            if (name.length() < LENGTH_THRESHOLD && !wordCount.containsKey(name)) {
                this.detectionData.addPayload(detectionData.entryName,name,"className");
            }
//            classCount++;
//            totalLengthClass += name.length();
            wordCount.merge(name, 1, Integer::sum);
        }
        if (!superName.isBlank()) {
            if (superName.length() < LENGTH_THRESHOLD && !wordCount.containsKey(name)) {
                this.detectionData.addPayload(detectionData.entryName,superName,"classSuperName");

            }
//            classCount++;
//            totalLengthClass += superName.length();
            wordCount.merge(superName, 1, Integer::sum);
        }
        for(String nameInterface: interfaces){
            if (!nameInterface.isBlank()) {
                if (nameInterface.length() < LENGTH_THRESHOLD && !wordCount.containsKey(name)) {
                    this.detectionData.addPayload(detectionData.entryName,nameInterface,"classInterfacerName");

                }
//                classCount++;
//                totalLengthClass += nameInterface.length();
                wordCount.merge(nameInterface, 1, Integer::sum);
            }
        }
        super.visit(version,access,name,signature,superName,interfaces);
    }

    @Override
    public FieldVisitor visitField(int access,
                                   String name,
                                   String descriptor,
                                   String signature,
                                   Object value){
        //this threshold is set in accordance with the paper "Detection of Obfuscation in Java Malware" by "Renuka Kumara, Anand Raj Essar Vaishakh"
        if(!name.isBlank()){
            if(name.length()<LENGTH_THRESHOLD && !wordCount.containsKey(name)){
                this.detectionData.addPayload(detectionData.entryName,name,"ClassField");
            }
//            fieldCount++;
//            totalLengthField+=name.length();
            wordCount.merge(name,1,Integer::sum);
        }
        return super.visitField(access,name,descriptor,signature,value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor base=super.visitMethod(access,name,descriptor,signature,exceptions);
        if (!name.isBlank()) {
            if (name.length() < LENGTH_THRESHOLD && !wordCount.containsKey(name)) {
                this.detectionData.addPayload(detectionData.entryName,name,"methodName");

            }
//            methodCount++;
//            totalLengthMethod += name.length();
            wordCount.merge(name, 1, Integer::sum);
        }


        return methodTraveller.create(this.api,base,this.detectionData, name+"-"+descriptor);
    }

    @Override
    public void visitEnd(){
        String pd="resultsClassLength";
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
            this.detectionData.addPayload(detectionData.entryName,pd,"Result");
        }
        super.visitEnd();
    }

}
