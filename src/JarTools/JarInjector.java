package JarTools;

import BytecodeIteration.InjectionClassTraveller;
import InjectionMethods.InjectionMethodEverywhere;
import InjectionMethods.InjectionMethodFirstMain;
import Main.Helper;
import BuildTools.PluginType;
import ObfuscationMethods.ObfuscatorInterface;
import ObfuscationMethods.ObfuscatorPrintB64;
import ObfuscationMethods.ObfuscatorSimplePrint;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.objectweb.asm.Opcodes.ASM9;

///Finds all the Jars inside a specific directory following build, creates the JarData Object and Injects payloads into a new
/// Jar located under data/Jars
public class JarInjector {

    boolean VERBOSE=false;

    //Obfuscators
    ArrayList<ObfuscatorInterface<Consumer<MethodVisitor>>> obfuscators=new ArrayList<>();

    ///all the methods to use are placed here
    public JarInjector(){
        obfuscators.add(new ObfuscatorSimplePrint(new InjectionMethodFirstMain()));
        obfuscators.add(new ObfuscatorPrintB64(new InjectionMethodFirstMain()));
    }

    /// Generates the JarTools.JarData by looking at the entrypoint of the major plugin loaders specified in PluginType
    ///
    /// So far, it's only able to read .yml and .json, with in theory .toml too but since the way Forge and NeoForge decide
    /// the entrypoint is different, there is little use at this point
    /// @param entries the JarFile to search the entrypoint of
    /// @return a JarTools.JarData object build based on the data found.
    public JarData generateData(JarFile entries){
        JarData outp=new JarData();
        //Plugin is fabric
        JarEntry test=null;

        //check for the type based on the entrypoint of each type
        for(PluginType a: PluginType.values()){
            test=entries.getJarEntry(a.entryPath);
            if (test!=null){
                outp.type=a;
                break;
            }
        }

        if(test!=null){
            //TODO: For now, this skips over Forge and NeoForge, in the future might be best to see if adding them is possible
            //TODO: There is currently no consideration for the possibility that a project compiles into multiple jars. Would assuming that the entrypoint remains unchanged wrong?
            if(!outp.type.entryName.isBlank()){
                //read the entry file and look for the specified entrypoint and add it to the outp.main
                try(BufferedReader bjs=new BufferedReader(new InputStreamReader(entries.getInputStream(test), StandardCharsets.UTF_8))){
                    String line;
                    while ((line=bjs.readLine())!=null){
                        if (line.contains(outp.type.entryName)){
                            //remove the prefix while taking into consideration that sometimes all could be in a single line
                            //TODO: make the system more robust
                            if (outp.type.entryPath.endsWith(".json")){
                                int begin=line.indexOf("\"main\":[\"")+"\"main\":[\"".length();
                                outp.main=line.substring(begin,line.indexOf("\"",begin)).strip().replace(".","/")+".class";
                                if(VERBOSE) Helper.printResultCyan("\t\tFound the .json ",outp.main);
                            }else if(outp.type.entryPath.endsWith(".yml")){
                                outp.main=line.substring(line.indexOf("main:")+"main:".length()).strip().replace(".","/")+".class";
                                if(VERBOSE) Helper.printResultCyan("\t\tFound the .yml ",outp.main);
                            }
                        }
                    }
                }catch (Exception e){
                    Helper.printRed("\tError (Injection): Exception arose on the inputStream of "+test.getRealName()+":\n"+e);                }
            }else if(VERBOSE){ Helper.printRed("\tNEOFORGE, FORGE 1.13+ or FORGE 1.12 or older"); }
        }else if(VERBOSE) Helper.printRed("\tError (Injection): No type matches the given jar");

        return outp;
    }




    /// Explores a given .jar
    ///
    /// @param toJar Path to the jar file to explore using JarFile
    ///
    /// TODO: add a filter method or class for every entry to decide when to apply what transformation / insertion / ecc.
    public JarData jarExplorer(Path toJar){
        if(VERBOSE) Helper.printResultCyan("\tFound a jar",toJar.toString());
        JarData outp=new JarData();
        try(JarFile jar=new JarFile(toJar.toString())){

            //create the JarTools.JarData of the file:
            outp=generateData(jar);

            try(JarOutputStream out=new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(Path.of("data/Jars/"+toJar.getFileName()))))){
                Enumeration<JarEntry> entries= jar.entries();
                while(entries.hasMoreElements()){
                    JarEntry entry=entries.nextElement();

                    //ASM loop where the produced payloads are consumed
                    if(entry.getRealName().endsWith(".class")){

                        //List of payloads to inject in the entry
                        List<Consumer<MethodVisitor>> classPayloads=new ArrayList<>();

                        //Check for applicability and if valid add it
                        for (ObfuscatorInterface<Consumer<MethodVisitor>> obf:obfuscators){
                            if(obf.isApplicable(entry,outp)) classPayloads.add(obf.generatePayload());
                        }

                        try(InputStream classStream= jar.getInputStream(entry)){
                            byte[] classBytes = classStream.readAllBytes();
                            ClassReader cr=new ClassReader(classBytes);
                            ClassWriter cw=new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                            InjectionClassTraveller ct=new InjectionClassTraveller(ASM9,cw,classPayloads);

                            cr.accept(ct,ClassReader.EXPAND_FRAMES);

                            byte[] res=cw.toByteArray();
                            out.putNextEntry(new JarEntry(entry.getName()));
                            out.write(res);
                            out.closeEntry();
                        }catch (Exception e){
                            Helper.printRed("\tError (Injection): Exception arose on the inputStream of "+entry.getRealName()+":\n"+e);
                        }

                        //Reset all the methods
                        for (ObfuscatorInterface<Consumer<MethodVisitor>> obf:obfuscators){
                            obf.reset();
                        }

                    }else{
                        out.putNextEntry(entry);
                        out.closeEntry();
                    }
                }
            } catch (Exception e) {
                Helper.printRed("\tError (Injection): Exception arose while reading the JarOutputStream of "+jar.getName()+": \n"+e);
            }
        }catch (Exception e){
            Helper.printRed("\tError (Injection): Exception arose while opening the jarFile with Path "+toJar+": \n"+e);
        }
        outp.filePath=Path.of("data/Jars/"+toJar.getFileName());
        return outp;
    }

    /// Set the variable VERBOSE in the object
    /// @param verbose true or false boolean value
    public void setVERBOSE(boolean verbose){
        this.VERBOSE=verbose;
    }

}
