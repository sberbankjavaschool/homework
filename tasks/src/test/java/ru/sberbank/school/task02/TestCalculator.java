package ru.sberbank.school.task02;

import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;
import ru.sberbank.school.task02.calculator.CurrencyCalculator;
import ru.sberbank.school.task02.calculator.CustomExternalQutesService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class TestCalculator {
    FxConversionService conversionService;
    ExternalQuotesService quotesService = new CustomExternalQutesService();
    ClientOperation sell = ClientOperation.SELL;
    ClientOperation buy = ClientOperation.BUY;

    @Before
    public void beforeCalculate() {
        conversionService = new CurrencyCalculator(quotesService);
    }

    @Test
    public void buyTest() {
        BigDecimal result = conversionService.convert(buy, Symbol.USD_RUB, BigDecimal.valueOf(100));
        Assert.assertTrue(result.compareTo(BigDecimal.valueOf(85)) == 0);
        result = conversionService.convert(buy, Symbol.USD_RUB, BigDecimal.valueOf(9.000000011581329E8));
        Assert.assertTrue(result.compareTo(BigDecimal.valueOf(82)) == 0);
    }

    @Test
    public void sellTest() {
        BigDecimal result = conversionService.convert(sell, Symbol.USD_RUB, BigDecimal.valueOf(100));
        Assert.assertTrue(result.compareTo(BigDecimal.valueOf(75)) == 0);
    }

}
