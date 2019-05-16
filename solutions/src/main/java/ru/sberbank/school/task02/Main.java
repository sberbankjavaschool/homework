package ru.sberbank.school.task02;

import org.apache.commons.cli.*;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();
        ClientController client = new ClientController(externalQuotesService);
        Options options = buildOptions();
        try {
            FxRequest request = getRequest(args, options);
            List<FxRequest> requests = new ArrayList<>();
            requests.add(request);
            requests.add(new FxRequest("USD/RUB", ClientOperation.SELL.toString(), "900"));
            List<FxResponse> responses = client.fetchResult(requests);
        } catch (ParseException | FxConversionException
                | IllegalArgumentException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Options buildOptions() {
        Option symbolOption = new Option("s", "symbol", true, "Symbol");
        Option operationOption = new Option("o", "operation", true, "Operation");
        Option amountOption = new Option("a", "amount", true, "Amount");
        symbolOption.setArgs(1);
        operationOption.setArgs(1);
        amountOption.setArgs(1);

        Options posixOptions = new Options();
        posixOptions.addOption(symbolOption);
        posixOptions.addOption(operationOption);
        posixOptions.addOption(amountOption);

        return posixOptions;
    }

    private static FxRequest getRequest(String[] args, Options posixOptions) throws ParseException {

        CommandLineParser cmdLinePosixParser = new DefaultParser();// создаем Posix парсер
        CommandLine commandLine = cmdLinePosixParser.parse(posixOptions, args);

        String symbol = "";
        String operation = "";
        String amount = "";

        if (commandLine.hasOption("s") && commandLine.hasOption("o")
                && commandLine.hasOption("a")) {
            symbol = commandLine.getOptionValues("s")[0];
            operation = commandLine.getOptionValues("o")[0];
            amount = commandLine.getOptionValues("a")[0];
        } else {
            throw new ParseException("Вы ввели не все параметры");
        }

        return new FxRequest(symbol, operation, amount);

    }
}