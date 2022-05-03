package ch.heigvd.dil.commands;

import ch.heigvd.dil.Utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "clean", description = "Clean a site (removing files under /build")
public class Clean implements Callable<Integer> {
  @CommandLine.Parameters(index = "0", paramLabel = "PATH", description = "Path to site folder")
  private Path target;

  @Override
  public Integer call() throws Exception {
    System.out.printf("Cleaning site at \"%s\" ...\n", target);

    if (!Files.isDirectory(target)) {
      System.out.printf(Utils.Messages.NOT_DIR, target);
      return 1;
    }

    Path build = target.resolve(Utils.Paths.BUILD_FOLDER);

    if (Files.isDirectory(build)) {
      FileUtils.cleanDirectory(build.toFile());
    }

    System.out.println("Cleaning successful!");
    return 0;
  }
}
