package ch.heigvd.dil.commands;

import ch.heigvd.dil.Config;
import ch.heigvd.dil.Page;
import ch.heigvd.dil.Utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import picocli.CommandLine.Command;

@Command(name = "init", description = "Initialize a static site project")
public class Init extends SiteCommand {
  public final String MAINPAGE_FILENAME = "index.md";

  @Override
  public Integer call() throws Exception {
    System.out.printf("Creating new site at \"%s\" ...\n", root);

    Path configFilePath = root.resolve(Utils.Paths.CONFIG_FILENAME);
    Path mainPagePath = root.resolve(MAINPAGE_FILENAME);

    if (!Files.exists(root)) {
      Files.createDirectory(root);
    } else if (root.toFile().list().length != 0) {
      System.out.printf(Utils.Messages.ALREADY_EXISTS, root);
      return 1;
    }

    Files.createFile(configFilePath);
    Files.createFile(mainPagePath);

    // Initialize site config
    Config config = new Config();
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
