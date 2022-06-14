package ch.heigvd.dil;

import java.io.*;
import java.util.concurrent.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class Utils {
  public static final String META_SEPARATOR = "---\n";
  public static final String TEMPLATES_SUFFIX = ".html";
  public static final String LAYOUT_TEMPLATE = "layout";

  public static void writeYamlFile(Object o, File filePath, Representer representer)
      throws FileNotFoundException {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setCanonical(false);
    options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

    if (representer == null) {
      representer = new Representer();
    }
    representer.addClassTag(Object.class, Tag.MAP);

    Yaml yaml = new Yaml(representer, options);

    PrintWriter writer = new PrintWriter(filePath);
    writer.write(yaml.dumpAs(o, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
    writer.close();
  }

  public static void appendToFile(String s, File filePath) throws IOException {
    FileWriter fw = new FileWriter(filePath, true);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(s);
    bw.close();
  }

  public static <T> T parseYamlFile(File filePath, Class<T> type) throws FileNotFoundException {
    return parseYaml(new FileInputStream(filePath), type);
  }

  public static <T> T parseYamlString(String yaml, Class<T> type) {
    return parseYaml(new ByteArrayInputStream(yaml.getBytes()), type);
  }

  public static <T> T parseYaml(InputStream stream, Class<T> type) {
    Yaml yaml = new Yaml(new Constructor(type));
    return yaml.load(stream);
  }

  public static class Debouncer {
    private static final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentHashMap<Object, Future<?>> delayedMap =
        new ConcurrentHashMap<>();

    /**
     * Debounces {@code callable} by {@code delay}, i.e., schedules it to be executed after {@code
     * delay}, or cancels its execution if the method is called with the same key within the {@code
     * delay} again.
     */
    public static void debounce(final Object key, final Runnable runnable, long delay) {
      final Future<?> prev =
          delayedMap.put(
              key,
              scheduler.schedule(
                  new Runnable() {
                    @Override
                    public void run() {
                      try {
                        runnable.run();
                      } finally {
                        delayedMap.remove(key);
                      }
                    }
                  },
                  delay,
                  TimeUnit.MILLISECONDS));
      if (prev != null) {
        prev.cancel(true);
      }
    }

    public void shutdown() {
      scheduler.shutdownNow();
    }
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
    public static final String TEMPLATE_FOLDER = "template";
  }
}
