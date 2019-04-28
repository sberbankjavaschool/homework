package ru.sberbank.school.task02;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

public class ConverterTest {
    @Test
    public void test() {
        ExternalQuotesService service = new ExternalQuotesServiceDemo();
        service.getQuotes(Symbol.USD_RUB)
                .forEach(
                        quote -> System.out.println(quote.getVolume() + " " + quote.getBid() + " " + quote.getOffer()));
        Converter conv = new Converter(service);
        BigDecimal convert = conv.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(100.00000000000000000000000003));
        System.out.println(convert);
        convert = conv.convert(ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(5333));
        System.out.println(convert);
    }
}
