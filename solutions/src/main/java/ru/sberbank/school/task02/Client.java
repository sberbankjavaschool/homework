package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.*;

public class Client {
    public static void main(String[] args) {
        try {
            FxClientController clientController = new FxClientControllerImpl(new ExternalQuotesServiceDemo());
            if (args.length != 3) {
                throw new IllegalArgumentException("Должно быть 3 аргумента");
            }
            checkArguments(args);
            String symbol = args[0];
            String direction = args[1];
            String amount = args[2];
            FxRequest request = new FxRequest(symbol, direction, amount);
            FxResponse response = clientController.fetchResult(request);
            System.out.println(response);
        } catch (RuntimeException e) {
            System.out.println("Произошла ошибка: \n" + e.getMessage());
        }
    }

    private static void checkArguments(String[] args) {
        String symbol = null;
        String direction = null;
        String amount = null;
        String error = "";
        for (String arg : args) {
            if (Symbol.RUB_USD.getSymbol().equals(arg.toUpperCase())
                    || Symbol.USD_RUB.getSymbol().equals(arg.toUpperCase())) {
                symbol = arg;
                continue;
            }
            try {
                ClientOperation.valueOf(arg.toUpperCase());
                direction = arg;
                continue;
            } catch (IllegalArgumentException ignore) {
                //
            }
            try {
                Double.valueOf(arg);
                amount = arg;
            } catch (NumberFormatException ignore) {
                //
            }
        }

        if (symbol == null) {
            error += "В аргументах нет валидной валютной пары \n";
        }
        if (direction == null) {
            error += "В аргументах не указана операция \n";
        }
        if (amount == null) {
            error += "В аргументах не указано количество";
        }

        if (symbol == null || direction == null || amount == null) {
            throw new IllegalArgumentException(error);
        } else {
            args[0] = symbol;
            args[1] = direction;
            args[2] = amount;
        }
    }
}
