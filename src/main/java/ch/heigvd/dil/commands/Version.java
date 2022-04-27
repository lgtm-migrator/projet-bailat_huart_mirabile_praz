package ch.heigvd.dil.commands;

import ch.heigvd.dil.Utils;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "-version")
public class Version implements Callable<Integer> {
  @Override
  public Integer call() throws Exception {
    System.out.println(Utils.getFullName());
    return 0;
  }
}
