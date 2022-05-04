package ch.heigvd.dil.commands;

import ch.heigvd.dil.Utils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "build")
public class Build implements Callable<Integer> {
  @Parameters(index = "0", paramLabel = "PATH", description = "Path to site folder")
  private Path root;

  private Path absoluteRoot;
  private Path build;
  private Path templates;
  private final Parser parser = Parser.builder().build();
  private final HtmlRenderer renderer = HtmlRenderer.builder().build();

  @Override
  public Integer call() throws Exception {
    System.out.printf("Building site at \"%s\" ...\n", root);

    absoluteRoot = root.toAbsolutePath();

    if (!Files.exists(root)) {
      System.out.printf(Utils.Messages.NOT_EXIST, absoluteRoot);
      return 1;
    }

    if (!Files.isDirectory(root)) {
      System.out.printf(Utils.Messages.NOT_DIR, root);
      return 1;
    }

    build = root.resolve(Utils.Paths.BUILD_FOLDER);
    templates = root.resolve(Utils.Paths.TEMPLATE_FOLDER);

    if (!Files.exists(build)) {
      Files.createDirectory(build);
    }

    Path config = root.resolve(Utils.Paths.CONFIG_FILENAME).toAbsolutePath();

    List<Path> excluded = List.of(build.toAbsolutePath(), config, templates.toAbsolutePath());

    List<Path> filtered =
        Files.list(root)
            .filter(v -> !excluded.contains(v.toAbsolutePath()))
            .collect(Collectors.toList());

    int result = 0;
    for (Path f : filtered) {
      int subResult = recursiveBuild(f.toAbsolutePath());
      if (subResult != 0) {
        result = subResult;
      }
    }

    if (result == 0) {
      System.out.println("Build successful!");
    }

    return result;
  }

  Integer recursiveBuild(final Path file) throws IOException {
    if (Files.isDirectory(file)) {
      for (Path f : Files.list(file).collect(Collectors.toList())) {
        int subResult = recursiveBuild(f.toAbsolutePath());
        if (subResult != 0) {
          return subResult;
        }
      }
    } else {
      String name = file.toString();
      String extension = FilenameUtils.getExtension(name);

      Path target = build.resolve(absoluteRoot.relativize(file));
      ensureParent(target);

      if ("md".equals(extension)) {
        target = Paths.get(target.toString().replaceAll(extension + "$", "html"));

        byte[] bytes = Files.readAllBytes(file);

        String fileContent = new String(bytes, StandardCharsets.UTF_8);

        int separatorIndex = fileContent.indexOf(Utils.META_SEPARATOR);

        if (separatorIndex == -1) {
          System.out.printf("File \"%s\" is not formatted correctly.\n", file);
          return 1;
        } else {
          String metadata =
              fileContent.substring(0, separatorIndex); // Metadata is currently ignored

          String content = fileContent.substring(separatorIndex + Utils.META_SEPARATOR.length());

          System.out.printf("Converting \"%s\".\n", absoluteRoot.relativize(file));
          String html = renderer.render(parser.parse(content));

          Files.write(target, html.getBytes(StandardCharsets.UTF_8));
        }
      } else {
        System.out.printf("Copying \"%s\".\n", absoluteRoot.relativize(file));
        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
      }
    }

    return 0;
  }

  void ensureParent(Path path) throws IOException {
    Path parent = path.getParent();
    if (!Files.exists(parent)) {
      Files.createDirectory(parent);
    }
  }
}
