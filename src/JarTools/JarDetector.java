package JarTools;

import BytecodeIteration.InjectionClassTraveller;
import DetectionTools.DetectionData;
import Main.Helper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarDetector {

    boolean VERBOSE=false;

    /// Set the variable VERBOSE in the object
    /// @param verbose true or false boolean value
    public void setVERBOSE(boolean verbose){
        this.VERBOSE=verbose;
    }

    public DetectionData jarExplorer(JarData data){
        DetectionData outp= new DetectionData();
        try(JarFile jar=new JarFile(data.filePath.toString())){
            Enumeration<JarEntry> entries= jar.entries();
            while(entries.hasMoreElements()){
                JarEntry entry=entries.nextElement();

            }
        }catch (Exception e){
            Helper.printRed("Error (Detection): Exception arose while opening the jarFile with Path "+data.filePath.toString()+": \n"+e);
        }

        return outp;
    }
}
