package ch.heigvd.dil.commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "build")
public class Build implements Callable<Integer> {
  static final String SEPARATOR = "---";

  @Parameters(index = "0")
  Path root;

  Path build;

  Parser parser = Parser.builder().build();
  HtmlRenderer renderer = HtmlRenderer.builder().build();

  @Override
  public Integer call() throws Exception {
    System.out.println("Building site......");

    if (Files.exists(root)) {
      if (Files.isDirectory(root)) {
        build = root.resolve("build");

        if (!Files.exists(build)) {
          Files.createDirectory(build);
        }

        int result = recursiveBuild(root);
        if (result == 0) {
          System.out.println("Build successful!");
        }

        return result;
      } else {
        System.out.printf("Specified path \"%s\" is not a directory.%n", root);
      }
    } else {
      System.out.printf("Specified path \"%s\" does not exist.%n", root.toAbsolutePath());
    }

    return 1;
  }

  Integer recursiveBuild(final Path file) throws IOException {
    if (Files.isDirectory(file)) {
      for (Path f :
          Files.list(file)
              .filter(v -> !v.toAbsolutePath().equals(build.toAbsolutePath()))
              .collect(Collectors.toList())) {
        int childRet = recursiveBuild(f.toAbsolutePath());
        if (childRet != 0) {
          return childRet;
        }
      }
    } else {
      String name = file.toString();
      String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

      Path target = build.resolve(root.toAbsolutePath().relativize(file));
      ensureParent(target);

      if ("md".equals(extension)) {
        target = Paths.get(target.toString().replaceAll(extension + "$", "html"));

        byte[] bytes = Files.readAllBytes(file);

        String fileContent = new String(bytes, StandardCharsets.UTF_8);

        int separatorIndex = fileContent.indexOf(SEPARATOR);

        if (separatorIndex == -1) {
          System.out.printf("File \"%s\" is not formatted correctly.%n", file);
          return 1;
        } else {
          String metadata =
              fileContent.substring(0, separatorIndex); // Metadata is currently ignored

          String content = fileContent.substring(separatorIndex + SEPARATOR.length());

          System.out.printf("Converting \"%s\".%n", root.toAbsolutePath().relativize(file));
          String html = renderer.render(parser.parse(content));

          Files.write(target, html.getBytes(StandardCharsets.UTF_8));
        }

      } else {
        System.out.printf("Copying \"%s\".%n", root.toAbsolutePath().relativize(file));
        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
      }
    }

    return 0;
  }

  void ensureParent(Path path) throws IOException {
    if (!Files.exists(path.getParent())) {
      Files.createDirectory(path.getParent());
    }
  }
}
