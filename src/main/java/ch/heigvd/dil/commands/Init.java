package ch.heigvd.dil.commands;

import ch.heigvd.dil.Config;
import ch.heigvd.dil.Page;
import ch.heigvd.dil.Utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "init", description = "Initialize a static site project")
public class Init implements Callable<Integer> {
  public final String MAINPAGE_FILENAME = "index.md";

  @Parameters(index = "0", paramLabel = "PATH", description = "Path to site folder")
  private String path;

  @Override
  public Integer call() throws Exception {
    System.out.printf("Creating new site at \"%s\" ...\n", path);

    Path sitePath = Paths.get(path);
    Path configFilePath = sitePath.resolve(Utils.Paths.CONFIG_FILENAME);
    Path mainPagePath = sitePath.resolve(MAINPAGE_FILENAME);

    if (Files.exists(sitePath)) {
      System.out.printf(Utils.Messages.ALREADY_EXISTS, sitePath);
      return 1;
    } else {
      Files.createDirectory(sitePath);
      Files.createFile(configFilePath);
      Files.createFile(mainPagePath);
    }

    // Initializes the sites config
    Config config = new Config();
    config.setSitePath(path);
    config.writeConfigFile(configFilePath.toFile());

    Page index = new Page();
    index.setTitle("Hello World");
    index.setAuthor("Statique Sitebuilder");

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    index.setDate(dateFormat.format(new Date()));

    index.setFilePath(mainPagePath);
    index.initPageFile();
    StringBuilder content = new StringBuilder();
    content.append(Utils.META_SEPARATOR).append("\n");
    content.append("# Hello World\n");
    content.append("## Bienvenue sur le générateur de sites web \"statique\"\n");
    content.append("Ceci est le premier article.\n");

    index.appendContent(content.toString());

    System.out.println("Creation successful!");
    return 0;
  }
}
