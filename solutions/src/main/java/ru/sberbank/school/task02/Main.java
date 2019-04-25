package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExternalQuotesService quotesService = new ExternalQuotesServiceDemo();
        FxConversionService calculator = serviceFactory.getFxConversionService(quotesService);

        System.out.println(calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, new BigDecimal(1000)));
    }

}
