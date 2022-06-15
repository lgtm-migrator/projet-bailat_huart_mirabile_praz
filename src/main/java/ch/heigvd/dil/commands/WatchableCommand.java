package ch.heigvd.dil.commands;

import picocli.CommandLine;

public abstract class WatchableCommand extends SiteCommand {
  @CommandLine.Option(
      names = {"--watch"},
      description = "Rebuild on file changes")
  protected boolean watch;
}
