package ch.heigvd.dil.commands;

import ch.heigvd.dil.Utils;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "serve", description = "Serve built site as a local website")
public class Serve extends WatchableCommand {
  public Javalin server;

  @Override
  public Integer call() {
    int result = new CommandLine(new Build()).execute(root.toString());

    if (result != 0) {
      return result;
    }

    try {
    // Serve locally
    server = Javalin.create(
            config -> {
              config.addStaticFiles(
                  root.resolve(Utils.Paths.BUILD_FOLDER).toAbsolutePath().toString(),
                  Location.EXTERNAL);
            })
        .start(8080);
    } catch (Exception e) {
      System.out.println(e);
      return 1;
    }

    if (watch) {
      // Build with watch
      new CommandLine(new Build()).execute(root.toString(), "--watch");
    }

    return 0;
  }
}
