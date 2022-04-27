package ch.heigvd.dil;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;

public class Utils {
  private static final String EXECUTABLE_NAME = "statique";
  private static final String VERSION = "0.0.1-pre_alpha";
  private static final String COPYRIGHT_TEXT = "Copyright © 2022 Bailat, Huart, Mirabile, Praz";
  public static final String META_SEPARATOR = "---\n";

  public static String getExecutableName() {
    return EXECUTABLE_NAME;
  }

  public static String getVersion() {
    return VERSION;
  }

  public static String getFullName() {
    return String.format("%s v%s\n%s", getExecutableName(), getVersion(), COPYRIGHT_TEXT);
  }

  public static void writeYamlFile(Object o, File filePath, Representer representer)
      throws FileNotFoundException {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setCanonical(false);
    options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

    Yaml yaml;

    if (representer == null) {
      yaml = new Yaml(options);
    } else {
      yaml = new Yaml(representer, options);
    }

    PrintWriter writer = new PrintWriter(filePath);
    yaml.dump(o, writer);
  }

  public static void appendToFile(String s, File filePath) throws IOException {
    FileWriter fw = new FileWriter(filePath, true);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(s);
    bw.close();
  }

  public static class Messages {
    public static final String NOT_DIR = "Specified path \"%s\" is not a directory.\n";
    public static final String NOT_EXIST = "Specified path \"%s\" does not exist.\n";
    public static final String ALREADY_EXISTS = "Specified path \"%s\" already exists.\n";
    public static final String NO_PATH = "Path not specified.\n";
  }

  public static class Paths {
    public static final String CONFIG_FILENAME = "config.yaml";
    public static final String BUILD_FOLDER = "build";
  }
}
