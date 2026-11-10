package ObfuscationManager;

import BytecodeIteration.Injection.InjectionClassTravellerAbstract;
import InjectionMethods.InjectionMethodInterface;
import JarTools.InjectionData;
import JarTools.JarEntryData;
import Main.Helper;
import PayloadGenerators.PayloadData;
import PayloadGenerators.PayloadGeneratorInterface;
import org.objectweb.asm.MethodVisitor;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class ObfuscationManagerBytecode extends ObfuscatorManagerBytecodeAbstract{

    private final List<PayloadGeneratorInterface<Consumer<MethodVisitor>>> payloadGenerators;
    private final List<InjectionMethodInterface> injectionMethods;
    private final List<String> bases;

    /// This class takes a series of generators to produce a payload to be consumed inside a method traveller's insert code.
    ///
    /// @param ict              the class traveller to be used for the bytecode insertion
    /// @param methodTraveller  the method traveller to be used for the bytecode insertion
    /// @param generators       the list of generators to be used to produce payloads.
    /// @param injectionMethods the list of injection methods. Each generator will be associated with an injectionMethod. If there are fewer methods than generators, the last method will be used for all remaining generators.
    /// @param bases            as above, list of strings to be passed to the generators. If fewer are passed, the last one will be repeated.
    public ObfuscationManagerBytecode(InitiateClassTraveller ict, InjectionClassTravellerAbstract.InitiateMethodTraveller methodTraveller, List<PayloadGeneratorInterface<Consumer<MethodVisitor>>> generators, List<InjectionMethodInterface> injectionMethods, List<String> bases) {
        super(ict, methodTraveller);
        this.payloadGenerators=generators;
        this.injectionMethods=injectionMethods;
        this.bases=bases;
    }

    @Override
    public void inject(JarFile jar, InjectionData data) {
        try(JarOutputStream out=new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(Path.of("data/Jars/"+Path.of(jar.getName()).getFileName()))))){
            //reset before a new loop to ensure accuracy
            for (InjectionMethodInterface injectionMethod:injectionMethods){
                injectionMethod.initiate();
            }
            Enumeration<JarEntry> entries= jar.entries();
            while(entries.hasMoreElements()){
                JarEntry entry=entries.nextElement();
                //ASM loop where the produced payloads are consumed
                if(entry.getRealName().endsWith(".class")){
                    //List of payloads to inject in the entry
                    List<PayloadData<Consumer<MethodVisitor>>> classPayloads=new ArrayList<>();

                    //Check for applicability and if valid add it
                    for(int i=0, j=0, l=0; i<payloadGenerators.size();i++){
                        if(injectionMethods.get(j).isConditionTrue(entry,data)){
                            classPayloads.add(payloadGenerators.get(i).producePayload(bases.get(l)));
                        }
                        if(j+1<injectionMethods.size()) j++;
                        if(l+1<bases.size()) l++;
                    }

                    try(InputStream classStream= jar.getInputStream(entry)){
                        JarEntryData entryData=new JarEntryData(entry.getRealName());
                        byte[] res=explore(classStream.readAllBytes(), classPayloads, entryData);
                        out.putNextEntry(new JarEntry(entry.getName()));
                        out.write(res);
                        out.closeEntry();
                        data.addEntry(entryData);
                    }catch (Exception e){
                        Helper.printRed("\tError (Injection): Exception arose on the inputStream of "+entry.getRealName()+":\n"+e);
                    }

                    //Reset all the methods
                    for (InjectionMethodInterface injectionMethod:injectionMethods){
                        injectionMethod.reset();
                    }

                }else{
                    out.putNextEntry(entry);
                    out.closeEntry();
                }
            }
        } catch (Exception e) {
            Helper.printRed("\tError (Injection): Exception arose while creating the JarOutputStream for "+Path.of(jar.getName()).getFileName()+": \n"+e);
        }
        data.filePath=Path.of("data/Jars/"+Path.of(jar.getName()).getFileName());
    }
}
