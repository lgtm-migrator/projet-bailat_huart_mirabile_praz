package ch.heigvd.dil;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.util.concurrent.Callable;

@Command(name = "sitegen")
public class Main implements Callable<Integer>
{
    public static void main( String[] args )
    {
        int exit = new CommandLine(new Main()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
