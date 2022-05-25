package ch.heigvd.dil.commands;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@State(Scope.Benchmark)
@CommandLine.Command(
    name = "benchmark",
    description = "Benchmark the application",
    subcommands = {Build.class})
public class Benchmark implements Callable<Integer> {
  @CommandLine.Parameters(index = "0", paramLabel = "PATH", description = "Path to site folder")
  private Path root;

  @org.openjdk.jmh.annotations.Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 2, time = 2)
  @Measurement(time = 2)
  @BenchmarkMode(Mode.AverageTime)
  public void evaluateBuild() {}

  public Integer call() {
    try {
      Main.main(new String[] {""});
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return 0;
  }
}
