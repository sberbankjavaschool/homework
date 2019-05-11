package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("There are must be 3 args");
        }

        ExternalQuotesService quotesService = new ExternalQuotesServiceDemo();
        FxClientController client = new FxClient(new FxConverter(quotesService));
        String symbol = args[0];
        String operation = args[1];
        String amount = args[2];

        FxRequest request = new FxRequest(symbol, operation, amount);

        FxResponse response = client.fetchResult(request);

        System.out.println(response);
    }
}
