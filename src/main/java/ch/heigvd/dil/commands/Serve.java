package ch.heigvd.dil.commands;

import ch.heigvd.dil.Utils;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "serve", description = "Serve built site as a local website")
public class Serve extends WatchableCommand {

  @Override
  public Integer call() {
    if (!watch) {
      // Build site
      new CommandLine(new Build()).execute(root.toString());
    }

    // Serve locally
    Javalin.create(
            config -> {
              config.addStaticFiles(
                  root.resolve(Utils.Paths.BUILD_FOLDER).toAbsolutePath().toString(),
                  Location.EXTERNAL);
            })
        .start(8080);

    if (watch) {
      // Build with watch
      new CommandLine(new Build()).execute(root.toString(), "--watch");
    }

    return 0;
  }
}
