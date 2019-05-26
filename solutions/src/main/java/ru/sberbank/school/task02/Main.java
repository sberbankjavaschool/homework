package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Main {
    public static void main(String[] args) {
        ExternalQuotesService service = new ExternalQuotesServiceDemo();
        ConsoleClient client = new ConsoleClient(service);
        FxRequest request = client.makeRequest(args);
        FxResponse response = client.fetchResult(request);
        System.out.println(response);
    }
}
