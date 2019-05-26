package ru.sberbank.school.task02.util;

import org.apache.commons.cli.*;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

    private final Options options;
    private final CommandLineParser parser;
    private final HelpFormatter helpFormatter;

    public ArgumentParser() {

        parser = new DefaultParser();
        helpFormatter = new HelpFormatter();
        options = new Options();
    }

    public ArgumentParser(Options options) {

        this();
        addOptions(options);
    }

    public Map<String, String> parseArgs(String[] args) {

        try {

            CommandLine cmd = parser.parse(options, args, true);
            Map<String, String> parsedArgs = new HashMap<>();

            for (Option option : options.getOptions()) {
                parsedArgs.put(option.getOpt(), option.hasArg()
                                                ? cmd.getOptionValue(option.getOpt())
                                                : "");
            }

            return parsedArgs;
        } catch (ParseException pe) {

            System.err.println(pe.getMessage());
            helpFormatter.printHelp("Client", options);
            throw new ConverterConfigurationException("try again");
        } catch (NullPointerException npe) {
            throw new ConverterConfigurationException("something went wrong");
        }
    }

    public void addOptions(Options options) {

        for (Option option : options.getOptions()) {
            this.options.addOption(option);
        }
    }
}
