package ru.sberbank.school.task02;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;


public class ConverterTests {

    @Test
    public void testLegal() {
        FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
        converter.convert(ClientOperation.BUY, Symbol.USD_RUB, new BigDecimal(100));
    }

    @Test(expected = FxConversionException.class)
    public void testEmptyQuotes() {
        FxConversionService converter = new FxConverter(symbol -> null);
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

    @Test(expected = NullPointerException.class)
    public void testNullAmount() {
        FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
        converter.convert(ClientOperation.BUY, Symbol.USD_RUB, null);
    }

    @Test
    public void testReturnCath() {
        BigDecimal b;
        try {
            b = new BigDecimal("-");
        } catch (IllegalArgumentException e) {
            b = BigDecimal.ZERO;
        }
        Assert.assertEquals(b.toString(), BigDecimal.ZERO.toString());
    }


}
