package ru.sberbank.school.task02.util;

import org.apache.commons.cli.*;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

    private final Options options;
    private final CommandLineParser parser;

    public ArgumentParser() {

        parser = new DefaultParser();
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
            return new HashMap<>();
        } catch (NullPointerException npe) {
            throw new ConverterConfigurationException(npe.getMessage());
        }
    }

    public void addOptions(Options opts) {

        for (Option opt : opts.getOptions()) {
            options.addOption(opt);
        }
    }
}
