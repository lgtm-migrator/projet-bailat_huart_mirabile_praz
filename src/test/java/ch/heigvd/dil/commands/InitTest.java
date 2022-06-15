package ch.heigvd.dil.commands;

import static org.junit.Assert.*;

import ch.heigvd.dil.Utils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import picocli.CommandLine;

public class InitTest {
  static final Path TEST_FOLDER = Path.of("test");

  @Test
  public void shouldReturnErrorIfPathAlreadyExist() throws IOException {
    System.out.println("-- PATH ALREADY EXISTS");

    // create new dir + add files
    TEST_FOLDER.toFile().mkdirs();
    new PrintWriter(TEST_FOLDER.resolve("rndFile.txt").toString(), StandardCharsets.UTF_8);
    int result = new CommandLine(new Init()).execute(TEST_FOLDER.toString());

    assertEquals(1, result);
  }

  @Test
  public void shouldContainsDefaultFiles() {

    System.out.println("-- CONTAINS DEFAULT FILES");

    // define which files should exist
    List<String> defaultfiles = Arrays.asList("config.yaml", "index.md");

    new CommandLine(new Init()).execute(TEST_FOLDER.toString());
    List files = List.of(TEST_FOLDER.toFile().list());

    assertTrue(files.containsAll(defaultfiles));
  }

  @Test
  public void configShouldNotBeEmpty() {

    System.out.println("-- CONFIG FILE NOT EMPTY");

    new CommandLine(new Init()).execute(TEST_FOLDER.toString());

    File configFile = TEST_FOLDER.resolve(Utils.Paths.CONFIG_FILENAME).toFile();

    assertNotEquals(0, configFile.length());
  }

  @After
  public void clean() {
    System.out.println("--- CLEANING ---");

    try {
      FileUtils.cleanDirectory(TEST_FOLDER.toFile());
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
