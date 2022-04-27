package ch.heigvd.dil;

import ch.heigvd.dil.commands.Build;
import ch.heigvd.dil.commands.Clean;
import ch.heigvd.dil.commands.Init;
import ch.heigvd.dil.commands.Version;
import picocli.CommandLine;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "statique",
    subcommands = {Init.class, Build.class, Clean.class, Version.class})
public class Main implements Callable<Integer> {
  public static void main(String[] args) {
    int rc = new CommandLine(new Main()).execute(args);
    System.exit(rc);
  }

  @Override
  public Integer call() {
    System.out.println("Unknown command, try 'build|clean|init|-version'");
    return 0;
  }
}
