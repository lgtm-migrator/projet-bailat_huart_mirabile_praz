package ch.heigvd.dil.commands;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import picocli.CommandLine;

import java.io.File;
import java.net.URISyntaxException;
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

  static final String JAR_PATH;

  static {
    try {
      JAR_PATH =
          new File(Benchmark.class.getProtectionDomain().getCodeSource().getLocation().toURI())
              .getPath();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 2, time = 2)
  @Measurement(time = 2)
  @BenchmarkMode(Mode.AverageTime)
  public void buildBenchmark() throws Exception {
    Runtime.getRuntime().exec(String.format("java -jar %s build %s", JAR_PATH, root));
  }

  public Integer call() throws Exception {
    Runtime.getRuntime().exec(String.format("java -jar %s init %s", JAR_PATH, root));
    Main.main(new String[] {""});

    return 0;
  }
}
