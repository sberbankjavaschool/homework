package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;


import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {

        ExternalQuotesService myExternalQuotesService = new MyExternalQuotesService();
        ServiceFactoryImpl serviceFactoryImpl = new ServiceFactoryImpl();
        ExtendedFxConversionService extendedConversionService =
                serviceFactoryImpl.getExtendedFxConversionService(myExternalQuotesService);

        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(0), Beneficiary.CLIENT));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(400), Beneficiary.CLIENT));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(5000), Beneficiary.CLIENT));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(25000), Beneficiary.CLIENT));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(68000), Beneficiary.CLIENT));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(5100), Beneficiary.CLIENT));

        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(0), Beneficiary.BANK));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(400), Beneficiary.BANK));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(5000), Beneficiary.BANK));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(25000), Beneficiary.BANK));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(68000), Beneficiary.BANK));
        System.out.println(extendedConversionService.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB,
                BigDecimal.valueOf(5100), Beneficiary.BANK));


    }
}
