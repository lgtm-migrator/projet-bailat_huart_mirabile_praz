package ch.heigvd.dil.commands;

import java.io.File;
import java.util.concurrent.Callable;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "clean", description = "Clean a site (removing files under /build")
public class Clean implements Callable<Integer> {

  @CommandLine.Parameters(paramLabel = "PATH", description = "Path to site")
  File[] directories;

  File buildDir;

  @Override
  public Integer call() throws Exception {

    // set a default value if no parameters
    // TODO: Try to use default value in CommandLine.parameter instead
    if (directories == null) {
      directories = new File[1];
      directories[0] = new File(System.getProperty("user.dir") + "/build");
    }

    for (File directory : directories) {
      buildDir = new File(directory.getAbsolutePath() + "/build");

      if (!buildDir.isDirectory()) {
        System.out.println("Oops ! " + buildDir.getPath() + " seems not to be a valid site path !");
        System.out.println("Nothing has been done.");
        return 1;
      }

      System.out.println("Cleaning site " + buildDir.getPath() + " ...");
      FileUtils.cleanDirectory(buildDir);
      System.out.println("Done !");
    }

    return 0;
  }
}
