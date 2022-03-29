package ch.heigvd.dil;

public class Utils {
    private static final String EXECUTABLE_NAME = "statique";
    private static final String VERSION = "0.1";

    public static String getExecutableName() {
        return EXECUTABLE_NAME;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getFullName() {
        return String.format("%s v%s\n", getExecutableName(), getVersion());
    }
}
