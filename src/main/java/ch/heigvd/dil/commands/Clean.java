package ch.heigvd.dil.commands;

import ch.heigvd.dil.Utils;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine.Command;

@Command(name = "clean", description = "Clean a site (removing files under /build)")
public class Clean extends SiteCommand {

  @Override
  public Integer call() throws Exception {
    System.out.printf("Cleaning site at \"%s\" ...\n", root);

    if (!Files.isDirectory(root)) {
      System.out.printf(Utils.Messages.NOT_DIR, root);
      return 1;
    }

    Path build = root.resolve(Utils.Paths.BUILD_FOLDER);

    if (Files.isDirectory(build)) {
      FileUtils.cleanDirectory(build.toFile());
    }

    System.out.println("Cleaning successful!");
    return 0;
  }
}
