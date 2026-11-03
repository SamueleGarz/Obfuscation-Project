package BuildTools;

///Enum with all the currently implemented tool
public enum BuildType {
    GRADLE("build/libs"),
    MAVEN("mavenBuildPath"),
    OTHER("");

    public final String buildDir;

    private BuildType(String buildDir) {
        this.buildDir = buildDir;
    }
}