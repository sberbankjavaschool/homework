package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExternalQuotesService quotesService = new MyExternalQuotesService();
        FxConversionService calculator = serviceFactory.getFxConversionService(quotesService);

        System.out.println(calculator.convert(null, Symbol.USD_RUB, new BigDecimal(1002.5320152644028)));
    }

}
