package ru.sberbank.school.task02;

import org.junit.Before;
import org.junit.Test;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;


public class ConverterTests {

    @Test
    public void testLegal() {
        FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
        converter.convert(ClientOperation.BUY, Symbol.USD_RUB, new BigDecimal(100));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSymbol() {
        FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
        converter.convert(ClientOperation.BUY, null, new BigDecimal(100));
    }

    @Test(expected = NullPointerException.class)
    public void testNullOperation() {
        FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
        converter.convert(null, Symbol.USD_RUB, new BigDecimal(100));
    }

    @Test
    public void testNullAmount() {
        FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
        converter.convert(ClientOperation.BUY, Symbol.USD_RUB, null);
    }

}
