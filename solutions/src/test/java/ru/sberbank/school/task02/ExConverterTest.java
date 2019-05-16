package ru.sberbank.school.task02;



import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;


import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

public class ExConverterTest {
    @Test
    public void test() {
        ExternalQuotesService service = new ExternalQuotesServiceDemo();
        service.getQuotes(Symbol.USD_RUB)
                .forEach(
                        quote -> System.out.println(quote.getVolume() + " " + quote.getBid() + " " + quote.getOffer()));
        ExConverter conv = new ExConverter(service);
        Optional<BigDecimal> convert = conv.convertReversed(ClientOperation.BUY,
                                                            Symbol.USD_RUB,
                                                            BigDecimal.valueOf(100),
                                                            Beneficiary.CLIENT);
        System.out.println(convert);
    }
}
