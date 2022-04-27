package ch.heigvd.dil.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "init")
public class Init implements Callable<Integer> {
  @Override
  public Integer call() throws Exception {
    System.out.println("Creating new site......");

    return 0;
  }
}
