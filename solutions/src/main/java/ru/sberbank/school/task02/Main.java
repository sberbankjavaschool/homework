package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        ExternalQuotesService myExternalQuotesService = new MyExternalQuotesService();
        ServiceFactoryImpl serviceFactoryImpl = new ServiceFactoryImpl();
        FxConversionService conversionService = serviceFactoryImpl.getFxConversionService(myExternalQuotesService);

        System.out.println(conversionService.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(0)));



    }
}
