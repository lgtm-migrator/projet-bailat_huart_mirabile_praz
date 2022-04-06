package ch.heigvd.dil.commands;

import ch.heigvd.dil.Config;
import ch.heigvd.dil.Page;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;
import java.util.Date;

@Command(name = "init")
public class Init implements Callable<Integer> {

    private final String CONFIG_FILENAME   = "config.yaml";
    private final String MAINPAGE_FILENAME = "index.md";

    @Parameters(index = "0", description = "Path to the website folder")
    private String path;

    @Override
    public Integer call() throws Exception {
        System.out.println("Creating new site...");
        Path sitePath = Paths.get(path);
        Path configFilePath = sitePath.resolve(CONFIG_FILENAME);
        Path mainPagePath = sitePath.resolve(MAINPAGE_FILENAME);

        if (Files.exists(sitePath)) {
            throw new FileAlreadyExistsException("The given path already exists.");
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
        content.append("---\n");
        content.append("# Hello World\n");
        content.append("## Bienvenue sur le générateur de sites web \"statique\"\n");
        content.append("Ceci est le premier article.\n");

        index.appendContent(content.toString());

        return 0;
    }
}
