package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Client {
    public static void main(String[] args) {
        FxClientController clientController = new FxClientControllerImpl(new ExternalQuotesServiceDemo());
        if (args.length != 3) {
            throw new IllegalArgumentException("Должно быть 3 аргумента");
        }
        String symbol = args[0];
        String direction = args[1];
        String amount = args[2];
        FxRequest request = new FxRequest(symbol, direction, amount);
        FxResponse response = clientController.fetchResult(request);
        System.out.println(response);
    }
}