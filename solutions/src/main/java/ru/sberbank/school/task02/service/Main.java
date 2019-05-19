package ru.sberbank.school.task02.service;


import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl();


        System.out.println(serviceFactory
                            .getFxConversionService(
                                    new ExternalQuotesServiceImpl())
                            .convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(100)));
    }
}
