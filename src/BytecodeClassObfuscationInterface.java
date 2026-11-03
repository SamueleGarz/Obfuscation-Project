import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;
import java.util.jar.JarEntry;

///Enum with all the currently implemented tool
enum PluginType {
    BUKKITSPIGOT("plugin.yml","main"),
    PAPER("paper-plugin.yml","main"),
    BUNGEECORDWATERFALL("bungee.yml","main"),
    VELOCITY("velocity-plugin.json","main"),
    SPONGE("META-INF/sponge_plugins.json","entrypoint"),
    FABRIC("fabric.mod.json","main"),
    QUILT("quilt.mod.json",""),

    //TODO: FOR THESE THREE THE MAIN MOD IS DETECTED BY THE ANNOTATED CLASS @Mod
    FORGENEW("META-INF/mods.toml",""),
    NEOFORGE("META-INF/neoforge.mods.toml",""),
    FORGEOLD("mcmod.info","");

    public final String entryPath;
    public final String entryName;

    private PluginType(String entryPath, String entryName) {
        this.entryPath = entryPath;
        this.entryName=entryName;
    }
}

///Interface for all the classes that will be used to generate a payload to be sent to a .class entry.
/// This class returns payloads to be consumed by a MethodVisitor in visitCode
///
/// ***The payloads are currently applied to every single method in the class file***
///
/// Preferably, each implementation should be named BytecodeClassObfuscation\<MethodName\>
///
/// TODO: Methods that might require the whole structure might need a future interface called JarFileInjectionInterface, to be seen based on research
public interface BytecodeClassObfuscationInterface {

    /// Check if the target is suitable for the injection
    ///
    /// For now, the logic of the insertion (example, random, only main class, ecc) should be implemented at this level
    /// as the bytecode injector runs on every entry and on every entry checks for applicability
    /// @param target the JarEntry to target
    /// @param targetData the JarData of the target
    /// @return A boolean value, true if the method is applicable
    boolean isApplicable(JarEntry target, JarData targetData);

    /// Returns a payload to inject into the bytecode
    ///
    /// The reason for this approach is that different implementation might require different pieces to be injected.
    /// The payloads are consumed inside ***MethodTraveller.visitCode()***
    /// @return A Consumer\<MethodVisitor\> structure that can be consumed by the injector to inject it
    Consumer<MethodVisitor> generatePayload();

}
