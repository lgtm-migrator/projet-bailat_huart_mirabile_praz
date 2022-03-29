package ch.heigvd.dil;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "sitegen")
public class SubcommandsMethods implements Callable<Integer> {

    @Command(name = "new")
    public Integer neew() {
        System.out.println("Creating new site......");
        return 0;
    }

    @Command(name = "clean")
    public Integer clean() {
        System.out.println("Cleaning site......");
        return 0;
    }

    @Command(name = "build")
    public Integer build() {
        System.out.println("Building site......");
        return 0;
    }

    @Command(name = "serve")
    public Integer serve() {
        System.out.println("Serving site......");
        return 0;
    }

    @Command(name = "-version")
    public Integer version() {
        System.out.println(Utils.getFullName());
        return 0;
    }

    @Override
    public Integer call() {
        System.out.println("Subcommand needed: 'new', 'clean', 'build' or 'serve'");
        return 0;
    }
}
