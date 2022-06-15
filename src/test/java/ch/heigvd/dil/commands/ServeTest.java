package ch.heigvd.dil.commands;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ServeTest {
  static final Path TEST_FOLDER = Path.of("test");

  Serve cmd;

  @Before
  public void cleanAndInit() throws InterruptedException {
    System.out.println("--- INIT ---");
    new CommandLine(new Clean()).execute(TEST_FOLDER.toString());
    new CommandLine(new Init()).execute(TEST_FOLDER.toString());
    cmd = new Serve();
  }

  @Test
  public void shouldExitIfNotExist() {
    System.out.println("-- NOT EXISTS");
    int result = new CommandLine(cmd).execute("FAIL");
    assertNotEquals(0, result);
  }

  @Test
  public void shouldServeOn8080() {
    System.out.println("-- SERVE ON 8080");
    new CommandLine(cmd).execute(TEST_FOLDER.toString());

    try {
      HttpRequest request =
          HttpRequest.newBuilder().uri(new URI("http://localhost:8080")).GET().build();
      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      assertTrue(response.body().startsWith("<"));

    } catch (Exception e) {
      fail(e.toString());
    }
  }

  @Test
  public void shouldExitIf8080Used() {
    System.out.println("-- EXIT ON 8080 USED");
    new CommandLine(cmd).execute(TEST_FOLDER.toString());

    int result = new CommandLine(new Serve()).execute(TEST_FOLDER.toString());
    assertNotEquals(0, result);
  }

  @After
  public void clean() {
    System.out.println("--- CLEANING ---");
    if (cmd.server != null) {
      cmd.server.stop();
    }

    try {
      FileUtils.cleanDirectory(TEST_FOLDER.toFile());
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
