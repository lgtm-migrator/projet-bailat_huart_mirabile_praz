package ch.heigvd.dil.commands;

import static org.junit.Assert.*;

import ch.heigvd.dil.Utils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

public class BuildTest {
  static final Path TEST_FOLDER = Path.of("test");

  @Before
  public void cleanAndInit() throws InterruptedException {
    System.out.println("--- INIT ---");
    new CommandLine(new Clean()).execute(TEST_FOLDER.toString());
    new CommandLine(new Init()).execute(TEST_FOLDER.toString());
  }

  @Test
  public void shouldExitIfNotExist() {
    System.out.println("-- NOT EXISTS");
    int result = new CommandLine(new Build()).execute("thisfolderiswrong");
    assertNotEquals(0, result);
  }

  @Test
  public void shouldCreateFolder() {
    System.out.println("-- CREATE FOLDER");
    new CommandLine(new Build()).execute(TEST_FOLDER.toString());
    assertTrue(Files.exists(TEST_FOLDER));
  }

  @Test
  public void shouldConvertMD() {
    System.out.println("-- CONVERT");
    new CommandLine(new Build()).execute(TEST_FOLDER.toString());

    Path index = TEST_FOLDER.resolve(Utils.Paths.BUILD_FOLDER).resolve("index.html");
    assertTrue(Files.exists(index));

    try {
      byte[] bytes = Files.readAllBytes(index);
      String fileContent = new String(bytes, StandardCharsets.UTF_8);
      assertTrue(fileContent.startsWith("<"));
    } catch (Exception e) {
      fail(e.toString());
    }
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
