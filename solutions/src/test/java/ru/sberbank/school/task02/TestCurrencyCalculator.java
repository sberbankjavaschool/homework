package ru.sberbank.school.task02;
import org.junit.*;
import ru.sberbank.school.task02.calculator.CurrencyCalculator;
import ru.sberbank.school.task02.calculator.CustomExternalQutesService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class TestCurrencyCalculator {
    FxConversionService conversionService;
    @Before
    public void initBefore() {
        conversionService = new CurrencyCalculator(new CustomExternalQutesService());

    }

    @Test
    public void TestBuyByValues() {
        BigDecimal hundred = conversionService.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(100));
        Assert.assertTrue(hundred.compareTo(BigDecimal.valueOf(75)) == 0);
        BigDecimal notValid =  conversionService.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(-1));
        Assert.assertTrue(notValid.compareTo(BigDecimal.valueOf(0)) == 0);
    }

    @Test
    public void TestSellByValues() {
        BigDecimal hundred = conversionService.convert(ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(100));
        Assert.assertTrue(hundred.compareTo(BigDecimal.valueOf(85)) == 0);
        BigDecimal notValid =  conversionService.convert(ClientOperation.SELL, Symbol.USD_RUB, BigDecimal.valueOf(-2));
        Assert.assertTrue(notValid.compareTo(BigDecimal.valueOf(0)) == 0);
    }
}
