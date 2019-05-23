package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Main {
    public static void main(String[] args) {

        ExternalQuotesService quotes = new ExternalQuotesServiceDemo();
        ServiceFactory factory = new CurrencyCalcFactory();
        FxConversionService calculator = factory.getFxConversionService(quotes);
        ClientController client = new ClientController(calculator);


        FxRequest request = client.makeRequest(args);
        FxResponse response = client.fetchResult(request);

        System.out.println(response);
    }

}