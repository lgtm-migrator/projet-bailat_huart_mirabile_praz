package ch.heigvd.dil;

import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class Utils {

  private static final String EXECUTABLE_NAME = "statique";
  private static final String VERSION = "0.0.1-pre_alpha";
  private static final String COPYRIGHT_TEXT = "Copyright © 2022 Bailat, Huart, Mirabile, Praz";

  public static String getExecutableName() {
    return EXECUTABLE_NAME;
  }

  public static String getVersion() {
    return VERSION;
  }

  public static String getFullName() {
    return String.format("%s v%s\n%s", getExecutableName(), getVersion(), COPYRIGHT_TEXT);
  }

  public static void writeYamlFile(Object o, File filePath) throws FileNotFoundException {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setCanonical(false);
    options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

    Representer representer =
        new Representer() {
          @Override
          protected Set<Property> getProperties(Class<?> type) {
            Set<Property> properties = super.getProperties(type);
            return properties.stream()
                .filter(v -> !v.getName().equals("filePath"))
                .collect(Collectors.toSet());
          }
        };
    representer.addClassTag(o.getClass(), Tag.MAP);

    Yaml yaml = new Yaml(representer, options);
    PrintWriter writer = new PrintWriter(filePath);
    yaml.dump(o, writer);
  }

  public static void appendToFile(String s, File filePath) throws IOException {
    FileWriter fw = new FileWriter(filePath, true);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(s);
    bw.close();
  }
}
