package ru.sberbank.school.task02;

import ru.sberbank.school.task02.services.MyExternalQuotesService;
import ru.sberbank.school.task02.services.ServiceFactoryImpl;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class TestCalculator {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExternalQuotesService quotesService = new MyExternalQuotesService();

        ExtendedFxConversionService calculator = serviceFactory.getExtendedFxConversionService(quotesService);

        System.out.println(calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                new BigDecimal(79_000_000),100,  Beneficiary.BANK));

        System.out.println(calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(99_000), 0,Beneficiary.CLIENT));

    }

}
