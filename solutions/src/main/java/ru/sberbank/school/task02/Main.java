package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        ServiceFactory sf = new ServiceFactoryImpl();
        Converter converter = (Converter) sf.getFxConversionService(new ExternalQuotesServiceDemo());
        System.out.println(converter.convert(ClientOperation.SELL,Symbol.USD_RUB,new BigDecimal(9.0E8)));

        ClientController client = new ClientController(new ExternalQuotesServiceDemo());
        FxRequest request = new FxRequest("USD/RUB","SELL","1000000000000");

        FxResponse response = client.fetchResult(request);
        System.out.println(response.getPrice());
    }
}
