package ru.sberbank.school.task02.service;


import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl();

        System.out.println(
                serviceFactory
                        .getExtendedFxConversionService(new ExternalQuotesServiceImpl())
                        .convertReversed(ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(77700), Beneficiary.CLIENT));

//        System.out.println(
//                serviceFactory
//                        .getFxConversionService(new ExternalQuotesServiceImpl())
//                        .convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1000000000)));
    }
}
