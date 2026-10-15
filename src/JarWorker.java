import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JarWorker {
    private List<String> sources=new ArrayList<>();

    //return the path to the entrypoint file in the given Jar
    private void jarExplorer(Path toJar){
        System.out.println("Found a jar: "+toJar.toString());
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
