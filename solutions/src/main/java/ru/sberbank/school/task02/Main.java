package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

/**
 * Created by Mart
 * 05.05.2019
 **/
public class Main {
    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();
        for (Quote q : externalQuotesService.getQuotes(Symbol.USD_RUB)) {
            System.out.println("offer: " + q.getOffer() +  " bid " + q.getBid()
                    + "  Volume: " + q.getVolumeSize());
        }
        System.out.println("========================================================");

        FxConversionService fxConversionService = serviceFactory.getFxConversionService(externalQuotesService);
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(600))
                + " expected 83 buy amount 600");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(5_000))
                + " expected 82 buy amount 5000");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(300_000))
                + " expected 83 buy amount 300 000");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(100))
                + " expected 84 buy amount 100");
        System.out.println(fxConversionService.convert(
                ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(100))
                + " expected 76 sell amount 100");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(9.000000044189101E8))
                + " expected 85 (but don't know why not 84) buy amount 9.000000044189101E8");
        System.out.println(fxConversionService.convert(
                ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(9.000000044189101E8))
                + " expected 75 sell amount 9.000000044189101E8");
        System.out.println(
                "10 compareTo 9: " + new BigDecimal(10).compareTo(new BigDecimal(9)));
    }
}
