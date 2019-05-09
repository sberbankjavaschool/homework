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
            FxRequest request = new FxRequest(args[0], args[1], args[2]);
            FxResponse response = client.fetchResult(request);
            System.out.println(response);
        } catch (ArrayIndexOutOfBoundsException arr) {
            throw new NullPointerException("There must be 3 arguments, but now it's only " + args.length);
        }
    }
}
