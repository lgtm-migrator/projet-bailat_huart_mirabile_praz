package ch.heigvd.dil;

import picocli.CommandLine;

public class Main{

  public static void main(String[] args) {
      int rc = new CommandLine(new SubcommandsMethods()).execute(args);
      System.exit(rc);
  }


}

