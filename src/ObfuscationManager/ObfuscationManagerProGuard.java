package ObfuscationManager;

import JarTools.InjectionData;
import Main.Helper;
import Main.commandExecute;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;

public class ObfuscationManagerProGuard implements ObfuscatorManagerInterface{

    private final String suffix;

    private HashMap<String, List<genericStorageStringBool>> dependencies;
    private List<String> visited;

    public ObfuscationManagerProGuard(String suffix){
        this.suffix=suffix;
    }


    private void recursiveRead(String key, List<String> list){
        if(dependencies.containsKey(key) && !visited.contains(key)){
            for(genericStorageStringBool val: dependencies.get(key)){
                if(val.check){
                    list.add(val.value);
                }else{
                    recursiveRead(val.value,list);
                }
            }
        }
    }

    public void writeToConfig(JarFile jar, InjectionData data, String configName){
        try(FileWriter writer=new FileWriter(configName)){
            writer.write("-injars "+Path.of(jar.getName()).toAbsolutePath()+"\n-outjars "+Path.of("data/Jars/"+suffix+ Path.of(jar.getName()).getFileName()).toAbsolutePath()+"\n");
            for(String entry: data.main){
                writer.write("-keep class "+entry.strip().replace(".class","").replace("/",".")+"\n");
            }

            if(!data.dependencies.isEmpty()){
                //escape from name/build/lib/
                Path name=Path.of(jar.getName()).toAbsolutePath().getParent().getParent().getParent();
                String obj=":";
                //this should just leave in temp the value that are also used in Gradle, matching the ProjectPath that follows CP
                String temp=data.source.toAbsolutePath().relativize(name).toString();
                if(!temp.isEmpty()) obj+=temp.replace(File.separatorChar,':');

                //In this case, check is used with false=DEP, true=CP
                this.dependencies=new HashMap<>();

                //avoid duplicates
                for(String a: data.dependencies.stream().distinct().toList()){

                    if(a.startsWith("CP") || a.startsWith("DEP")){
                        String[] parts=a.split("\t",3);
                        //something was wrong with the format / it's not a valid entry considering the fact that it should be CP\tProjectPath\tJarPath
                        if(parts.length<3) continue;
                        if(!parts[2].endsWith(".jar") && parts[0].startsWith("CP")){
                            if(!Files.exists(Path.of(parts[2]))){
                                continue;
                            }
                        }
                        String projectPath=parts[1];
                        if(dependencies.containsKey(projectPath)){
                            dependencies.get(projectPath).add(new genericStorageStringBool(parts[2],parts[0].startsWith("CP")));
                        }else{
                            dependencies.put(projectPath,new ArrayList<>());
                            dependencies.get(projectPath).add(new genericStorageStringBool(parts[2],parts[0].startsWith("CP")));
                        }
                    }
                    else if(a.startsWith("DIR")){
                        String[] parts=a.split("\t",3);
                        //something was wrong with the format / it's not a valid entry considering the fact that it should be CP\tProjectPath\tJarPath
                        if(parts.length<3) continue;
                        String key=":"+Path.of(parts[2]).toAbsolutePath().relativize(name).toString().replace(File.separatorChar,':');
                        if(dependencies.containsKey(key)){
                            dependencies.get(key).add(new genericStorageStringBool(parts[1],false));
                        }else{
                            dependencies.put(key,new ArrayList<>());
                            dependencies.get(key).add(new genericStorageStringBool(parts[1],false));
                        }
                    }
                    else{
                        writer.write("-libraryjars "+a+"\n");
                    }
                }

                List<String> toWrite=new ArrayList<>();
                this.visited=new ArrayList<>();

                for(String key:dependencies.keySet()){
                    if(key.equals(obj) || key.equals(":")){
                        visited.add(key);
                        for(genericStorageStringBool val: dependencies.get(key)){
                            if(val.check){
                                toWrite.add(val.value);
                            }else{
                                recursiveRead(val.value,toWrite);
                            }
                        }
                    }
                }

                for(String a:toWrite.stream().distinct().toList()){
                    writer.write("-libraryjars "+a+"\n");
                }
            }
        }catch (Exception e){
            Helper.printRed("Error during ProGuard injection into "+jar.getName()+":\n"+e);
        }
    }

    @Override
    public void inject(JarFile jar, InjectionData data) {
        //if there is no entry point there is no reason to inject since it's either an unsopported plugin type or a jar that was not build by me.
        //for the same reason,
        if(data.main.isEmpty()) return;

        //First, build the config.pro file
        String configName="data/Outputs/Injection/Proguard/config"+jar.toString().replace(".","").replace("@","")+".pro";
        writeToConfig(jar,data,configName);

        if (commandExecute.runComm(new File(System.getProperty("user.dir")),new String[]{"java", "-jar","lib/proguard-7.10.0/lib/proguard.jar","@"+configName},null) == 0) {
            Helper.printResultCyan("\t\tOutput: ","worked");
        }
    }
}
