package ch.heigvd.dil.commands;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

public class CleanTest {
  private static final Path TEST_FOLDER = Path.of("cleantest");

  @Before
  public void cleanAndInit() {
    System.out.println("=== INIT ===");
    new CommandLine(new Clean()).execute(TEST_FOLDER.toString());
    new CommandLine(new Init()).execute(TEST_FOLDER.toString());
    new CommandLine(new Build()).execute(TEST_FOLDER.toString());
  }

  @Test
  public void shouldExitIfNotExist() {
    System.out.println("== NOT EXISTS");
    int result = new CommandLine(new Clean()).execute("thisfolderiswrong");
    assertNotEquals(0, result);
  }

  @Test
  public void shouldCleanBuildFolder() {
    System.out.println("=== CLEAN");
    new CommandLine(new Clean()).execute(TEST_FOLDER.toString());
    try {
      boolean result = FileUtils.isEmptyDirectory(TEST_FOLDER.resolve("build").toFile());
      assertTrue(result);
    } catch (IOException e) {
      fail(e.toString());
    }
  }

  @After
  public void cleanAndGoodbye() {
    System.out.println("=== CLEANING ===");
    try {
      FileUtils.cleanDirectory(TEST_FOLDER.toFile());
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
