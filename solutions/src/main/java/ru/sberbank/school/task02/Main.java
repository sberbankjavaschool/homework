package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.util.*;

public class Main {
    public static void main(String[] args) {

        if (args.length < 3) {
            throw new ConverterConfigurationException("Вы ввели мало параметров!");
        }

        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();
        ClientController client = new ClientController(externalQuotesService);
        String symbol = args[0];
        String operation = args[1];
        String amount = args[2];

        FxRequest request = new FxRequest(symbol, operation, amount);

        FxResponse response = client.fetchResult(request);

        System.out.println(response);
    }
}
