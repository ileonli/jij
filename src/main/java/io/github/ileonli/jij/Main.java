package io.github.ileonli.jij;

import io.github.ileonli.jij.prog.JavapArgumentAction;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("jij").build()
                .description("JVM in Java");

        parser.addArgument("--javap").type(String.class).action(new JavapArgumentAction());

        try {
            Namespace res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}
