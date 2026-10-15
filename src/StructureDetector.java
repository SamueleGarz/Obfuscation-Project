import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class StructureDetector {

    public String findDeclaration(File target){
        if (target==null) return "-1";
        if (target.isDirectory()){
            File[] files=target.listFiles();
            if (files==null) return "-1";
            for (File file : files){
                if (file.isFile()){
                    //Bukkit, Spigot, Paper, BungeeCord (not fully, missing bungee.yml)
                    if (file.getName().contains("plugin.yml")){
                        try (Scanner reader = new Scanner(file)){
                            while (reader.hasNextLine()){
                                String line=reader.nextLine();
                                if (line.contains("main")){
                                    return line.replace("main:","").strip();
                                }
                            }
                        } catch (FileNotFoundException e){
                            System.out.println("Missing file");
                            e.printStackTrace();
                        }

                    }
                    //Velocity
                    if (file.getName().contains("plugin.json")){
                        try (Scanner reader = new Scanner(file)){
                            while (reader.hasNextLine()){
                                String line=reader.nextLine();
                                if (line.contains("main")){
                                    return line.replace("main:","").replace("\"","").replace(",","").strip();
                                }
                            }
                        } catch (FileNotFoundException e){
                            System.out.println("Missing file");
                            e.printStackTrace();
                        }
                    }
                    //Fabric
                    if (file.getName().contains("fabric.mod.json")){
                        boolean mainEntry=false;
                        try (Scanner reader = new Scanner(file)){
                            while (reader.hasNextLine()){
                                String line=reader.nextLine();
                                //Not enough, might need to check for client and server too for more sprcific scenarios
                                //see veinminer-client fabric resources fabric-mod.json
                                if (line.contains("main")){
                                    mainEntry=true;
                                }else if (mainEntry){
                                    if (!line.strip().isBlank()){
                                        return line.replace("\"","").strip();
                                    }
                                }
                            }
                        } catch (FileNotFoundException e){
                            System.out.println("Missing file");
                            e.printStackTrace();
                        }
                    }
                    //Forge, Neoforge
                    if (file.getName().contains("mods.toml")){
                        System.out.println("Forge, needs to be investigated");

                    }
                }else if(file.isDirectory()){
                    String ret=findDeclaration(file);
                    if (ret!="-1") return ret;
                }
            }
        }
        return "-1";
    }

}
