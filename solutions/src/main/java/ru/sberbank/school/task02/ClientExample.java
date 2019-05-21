package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class ClientExample {
    public static void main(String[] args) {
        ExtendedFxConversionService calculator = new ServiceFactoryForCurrCalc()
                .getExtendedFxConversionService(new ExternalQuotesServiceDemo());

        Client client = new Client(calculator);


        try {
            String symbol = args[0];
            String direction = args[1];
            String amount = args[2];
            FxRequest request = new FxRequest(symbol, direction, amount);
            FxResponse response = client.fetchResult(request);
            System.out.println(response);
        } catch (ArrayIndexOutOfBoundsException arr) {
            throw new NullPointerException("There must be 3 arguments, but now it's only " + args.length);
        }
    }
}
