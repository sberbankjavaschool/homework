package ru.sberbank.school.task02;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {

        ServiceFactory factory = new ServiceFactoryC();
        ExternalQuotesService quotesService = new ExternalQuotesServiceC();

        FxConversionService exCalc = factory.getFxConversionService(quotesService);
        BigDecimal offer = exCalc.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(100));
        System.out.println(offer);

    }
}
