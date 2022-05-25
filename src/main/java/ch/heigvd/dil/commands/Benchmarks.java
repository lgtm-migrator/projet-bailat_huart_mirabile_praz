package ch.heigvd.dil.commands;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

import java.io.IOException;

public class Benchmarks {

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(value = 1)
    public void cool() {

    }

    public static void executeAll() {
        String[] args = new String[]{""};
        try {
            org.openjdk.jmh.Main.main(args);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
