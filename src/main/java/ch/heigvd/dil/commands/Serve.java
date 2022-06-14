package ch.heigvd.dil.commands;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "serve", description = "Serve built site as a local website")
public class Serve implements Callable<Integer> {

  @Parameters(index = "0", description = "Path to the website folder")
  private Path path;

  @CommandLine.Option(
      names = {"--watch"},
      description = "Rebuild on file changes")
  private boolean watch;

  @Override
  public Integer call() {
    if (!watch) {
      // Build the site
      new CommandLine(new Build()).execute(path.toString());
    }

    // Serve the site
    Javalin.create(
            config -> {
              config.addStaticFiles(
                  path.resolve("build").toAbsolutePath().toString(), Location.EXTERNAL);
            })
        .start(8080);

    if (watch) {
      // Build with watch
      new CommandLine(new Build()).execute(path.toString(), "--watch");
    }

    return 0;
  }
}
