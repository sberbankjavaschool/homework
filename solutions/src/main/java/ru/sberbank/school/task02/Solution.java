package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Solution {
    public static void main(String[] args) {

        try {
            CalculatorFactory calculatorFactory = new CalculatorFactory();
            FxConversionService calculator =
                    calculatorFactory.getFxConversionService(new ExternalQuotesServiceDemo());
            Client client = new Client(calculator);

            FxRequest request = parseArgs(args);
            FxResponse response = client.fetchResult(request);
            System.out.println(response.toString());
        } catch (FxConversionException ex) {
            System.out.println("Error: " + ex.toString());
        }

    }

    private static FxRequest parseArgs(String[] args) {

        if (args.length == 0) {
            showUsage();
            System.exit(0);
        }

        String symbol = null;
        String operation = null;
        String amount = null;

        int i = 0;

        try {
            while (i < args.length) {
                if (args[i].equalsIgnoreCase("--symbol")) {
                    symbol = args[i + 1];
                    i += 2;
                } else if (args[i].equalsIgnoreCase("--operation")) {
                    operation = args[i + 1];
                    i += 2;
                } else if (args[i].equalsIgnoreCase("--amount")) {
                    amount = args[i + 1];
                    i += 2;
                } else {
                    System.out.println("Unknows parameter: " + args[i]);
                    showUsage();
                    System.exit(1);
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("No value for parameter: " + args[i]);
            showUsage();
            System.exit(1);
        }

        if (symbol == null || operation == null || amount == null) {
            System.out.println("Not all parameters specified!");
            showUsage();
            System.exit(1);
        }
        return new FxRequest(symbol, operation, amount);

    }

    private static void showUsage() {

        System.out.println("Usage: name program");
        System.out.println("  --symbol     Trade instrument");
        System.out.println("  --operation  Operation: BUY, SELL");
        System.out.println("  --amount     Quantity to exchange");
        System.out.println("Example:");
        System.out.println("  java Solution --symbol USD/RUB --operation BUY -- amount 340");

    }

}
