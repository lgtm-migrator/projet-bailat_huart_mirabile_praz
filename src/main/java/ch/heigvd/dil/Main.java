package ch.heigvd.dil;

import ch.heigvd.dil.commands.*;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "statique",
    subcommands = {Init.class, Build.class, Clean.class, Serve.class, Publish.class, Benchmark.class},
    versionProvider = Main.ManifestVersionProvider.class)
public class Main implements Callable<Integer> {
  @CommandLine.Option(
      names = {"-v", "--version"},
      versionHelp = true,
      description = "Print version info")
  boolean versionRequested;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new Main()).execute(args);
    if (exitCode != 0) {
      System.exit(exitCode);
    }
  }

  @Override
  public Integer call() {
    CommandLine.usage(this, System.out);
    return 0;
  }

  static class ManifestVersionProvider implements CommandLine.IVersionProvider {
    public String[] getVersion() {
      return new String[] {getClass().getPackage().getImplementationVersion()};
    }
  }
}
