import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarWorker {

    boolean VERBOSE=false;
    /// Explores a given .jar
    ///
    /// @param toJar Path to the jar file to explore using JarFile
    ///
    /// TODO: add a filter method or class for every entry to decide when to apply what transformation / insertion / ecc.
    private void jarExplorer(Path toJar){
        if(VERBOSE) System.out.println("\tFound a jar: "+toJar.toString());
        try(JarFile jar=new JarFile(toJar.toString())){
            try(JarOutputStream out=new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(Path.of("data/Jars/"+toJar.getFileName()))))){

                Enumeration<JarEntry> entries= jar.entries();
                while(entries.hasMoreElements()){
                    JarEntry entry=entries.nextElement();
                    //any file should be ok to inject into since it's static analysis
                    //TODO: improve the following by adding a filter class / more checks / a is applicable function for every different methods to be run
                    if(entry.getName().endsWith(".class")){
                        try(InputStream classStream= jar.getInputStream(entry)){
                            byte[] classBytes = classStream.readAllBytes();
                            ClassReader cr=new ClassReader(classBytes);
                            ClassWriter cw=new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
                            ClassTraveller ct=new ClassTraveller(ASM9,cw);

                            cr.accept(ct,ClassReader.EXPAND_FRAMES);

                            byte[] res=cw.toByteArray();
                            out.putNextEntry(new JarEntry(entry.getName()));
                            out.write(res);
                            out.closeEntry();
                        }catch (Exception e){
                            System.err.println("Exception arose on the inputStream of "+entry.getRealName()+":\n"+e);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception arose while reading the JarOutputStream of "+jar.getName()+": \n"+e);
            }
        }catch (Exception e){
            System.err.println("Exception arose while opening the jarFile with Path "+toJar+": \n"+e);
        }
    }

    /// Locate all the jar files inside a given target
    /// @param start Path of the directory to start the walk from
    /// @param buildParent Filter for the walk; only files that include _buildParent_ in their path will be observed
    ///
    public void jar_finder(Path start, String buildParent){
        try{
            Files.walk(start).filter(path -> path.toString().endsWith(".jar") && path.getParent().toString().contains(buildParent)).forEach(this::jarExplorer);
        } catch (Exception e){
            System.err.println("Exception arose during the walk of Path "+start+": \n"+e);
        }
    }

    /// Set the variable VERBOSE in the object
    /// @param verbose true or false boolean value
    public void setVERBOSE(boolean verbose){
        this.VERBOSE=verbose;
    }

}
