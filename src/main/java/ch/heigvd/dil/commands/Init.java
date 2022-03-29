package ch.heigvd.dil.commands;

import picocli.CommandLine.Command;
import java.util.concurrent.Callable;

@Command(name = "init")
public class Init implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Creating new site......");

        return 0;
    }
}
