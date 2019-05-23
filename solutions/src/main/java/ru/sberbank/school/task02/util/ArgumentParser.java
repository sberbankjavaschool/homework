package ru.sberbank.school.task02.util;

import org.apache.commons.cli.*;

public class ArgumentParser {

    private final Options options;
    private final CommandLineParser parser;
    private final HelpFormatter helpFormatter;

    public ArgumentParser() {

        options = new Options();
        options.addRequiredOption("s", "symbol", true, "Currency symbol");
        options.addRequiredOption("d", "direction", true, "Desired operation");
        options.addRequiredOption("a", "amount", true, "Desired amount");

        parser = new DefaultParser();
        helpFormatter = new HelpFormatter();
    }

    public CommandLine parseArgs(String[] args) {

        try {
            return parser.parse(options, args, false);
        } catch (ParseException pe) {

            System.out.println(pe.getMessage());
            helpFormatter.printHelp("Client", options);

            return null;
        }
    }
}
