package ch.heigvd.dil;

import ch.heigvd.dil.commands.*;
import picocli.CommandLine;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "statique",
subcommands = {
        Init.class,
        Build.class,
        Clean.class,
        Version.class
})
public class Main implements Callable<Integer> {

    public static void main(String[] args) {
        int rc = new CommandLine(new Main()).execute(args);
        System.exit(rc);
    }

    @Override
    public Integer call() {
        System.out.println("Subcommand needed: 'init', 'clean' or 'build'");
        return 0;
    }
}
