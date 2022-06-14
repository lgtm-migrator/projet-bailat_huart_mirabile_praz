package ch.heigvd.dil.commands;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;

public abstract class SiteCommand implements Callable<Integer> {
  @CommandLine.Parameters(index = "0", paramLabel = "PATH", description = "Path to website folder")
  protected Path root;
}
