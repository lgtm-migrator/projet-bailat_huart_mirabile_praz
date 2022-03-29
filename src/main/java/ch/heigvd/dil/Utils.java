package ch.heigvd.dil;

public class Utils {
    private static final String EXECUTABLE_NAME = "statique";
    private static final String VERSION = "0.0.1-pre_alpha";
    private static final String COPYRIGHT_TEXT = "Copyright © 2022 Bailat, Huart, Mirabile, Praz";

    public static String getVersion() {
        return VERSION;
    }

    public static String getExecutableName() {
        return EXECUTABLE_NAME;
    }

    public static String getFullName() {
        return String.format("%s v%s\n%s\n", getExecutableName(), getVersion(), COPYRIGHT_TEXT);
    }
}
