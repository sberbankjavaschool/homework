package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Optional;

public class Test {
    public static void main(String[] args) {

        ServiceFactory factory = new ServiceFactoryImpl();
        ExternalQuotesService quotesService = new ExternalQuotesServiceImpl();
        FxConversionService exCalc = factory.getFxConversionService(quotesService);
        BigDecimal offer = exCalc.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(104.7559849936236));
        System.out.println(offer);

        ExtendedFxConversionService exCalc2 = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> revOffer = exCalc2.convertReversed(ClientOperation.BUY,
                Symbol.USD_RUB, BigDecimal.valueOf(10000), Beneficiary.BANK);
        System.out.println(revOffer);
    }
}
