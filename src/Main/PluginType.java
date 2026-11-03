package Main;

///Enum with all the currently implemented tool
public enum PluginType {
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
    FORGEOLD("mcmod.info",""),
    OTHER("","");

    public final String entryPath;
    public final String entryName;

    private PluginType(String entryPath, String entryName) {
        this.entryPath=entryPath;
        this.entryName=entryName;
    }
}