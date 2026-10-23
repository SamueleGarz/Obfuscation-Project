import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarWorker {

    //return the path to the entrypoint file in the given Jar
    private void jarExplorer(Path toJar){
        System.out.println("Found a jar: "+toJar.toString());
        try(JarFile jar=new JarFile(toJar.toString())){
            try(JarOutputStream out=new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(Path.of("data/Jars/"+toJar.getFileName()))))){
                Enumeration<JarEntry> entries= jar.entries();
                while(entries.hasMoreElements()){
                    JarEntry entry=entries.nextElement();
                    if(entry.getName().endsWith(".class")){
                        //any file should be ok to inject into since it's static analysis
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
                            System.out.println("Issues while reading the input stream");
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Issues with the output stream");
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println("Issue opening the jar: "+toJar.toString());
            e.printStackTrace();
        }
    }

    public void jarFinder(File target,String buildParent){
        Path start= Paths.get(target.getPath());
        try{
            Files.walk(start).filter(path -> path.toString().endsWith(".jar") && path.getParent().toString().contains(buildParent)).forEach(this::jarExplorer);
        } catch (Exception e){
            System.out.println("Issue during gradle walk");
        }
    }

}
