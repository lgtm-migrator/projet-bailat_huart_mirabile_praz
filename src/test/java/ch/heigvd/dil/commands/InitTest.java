package ch.heigvd.dil.commands;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InitTest {


    static final String PATHDIR = "./testSite";

    @Test
    public void shouldReturnErrorIfPathAlreadyExist() throws IOException {
        System.out.println("-- PATH ALREADY EXISTS");

        //create new dir + add files
        new File(PATHDIR).mkdirs();
        new PrintWriter(PATHDIR + "/rndFile.txt", "UTF-8");
        int result = new CommandLine(new Init()).execute(PATHDIR);

        //delete folder and files
        deleteDirectory(new File(PATHDIR));

        assertEquals(1, result);
    }

    @Test
    public void shouldContainsDefaultFiles() throws IOException {

        System.out.println("-- CONTAINS DEFAULT FILES");

        //define which files should exist
        List<String> defaultfiles = Arrays.asList("config.yaml", "index.md");
        Path dirPath = Path.of(PATHDIR);

        new CommandLine(new Init()).execute(dirPath.toString());
        List files = List.of(dirPath.toFile().list());

        //delete folder and files
        deleteDirectory(new File(dirPath.toString()));

        assertEquals(true, files.containsAll(defaultfiles));

    }

    @Test
    public void configShouldNotBeEmpty() throws IOException {

        System.out.println("-- CONFIG FILE NOT EMPTY");

        Path dirPath = Path.of(PATHDIR);
        new CommandLine(new Init()).execute(dirPath.toString());

        File configFile = new File(PATHDIR + "/config.yaml");

        //delete folder and files
        deleteDirectory(new File(PATHDIR));

        assertNotEquals(0, configFile.length());

    }

}
