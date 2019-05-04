package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        ServiceFactory sf = new ServiceFactoryImpl();
        Converter converter = (Converter) sf.getFxConversionService(new ExternalQuotesServiceDemo());
        System.out.println(converter.convert(ClientOperation.SELL,Symbol.USD_RUB,new BigDecimal(9.0E8)));
    }
}
