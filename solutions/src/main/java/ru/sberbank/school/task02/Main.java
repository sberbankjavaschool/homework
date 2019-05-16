package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Main {

    public static void main(String[] args) {

        FxClientControllerImpl client = new FxClientControllerImpl(new ExternalQuotesServiceDemo());
        FxResponse response = client.fetchResult(new FxRequest(args[0], args[1], args[2]));

        System.out.println(response);
    }

}
