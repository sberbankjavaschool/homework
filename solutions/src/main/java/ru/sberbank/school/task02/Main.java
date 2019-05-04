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
            System.out.println("offer: " + q.getOffer() + "  Volume: " + q.getVolumeSize());
        }
        System.out.println("========================================================");

        FxConversionService fxConversionService = serviceFactory.getFxConversionService(externalQuotesService);
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(600)) + " amount 600");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(5_000)) + " amount 5000");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(300_000)) + " amount 300 000");
        System.out.println(fxConversionService.convert(
                ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(100)) + " amount 100");
        System.out.println(fxConversionService.convert(
                ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(100)) + " amount 100");
    }
}
