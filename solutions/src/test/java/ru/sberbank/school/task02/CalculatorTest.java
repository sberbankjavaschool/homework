package ru.sberbank.school.task02;

import org.junit.*;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class CalculatorTest {
    private ServiceFactory factory = new ServiceFactoryProvider();
    private ExternalQuotesService quotesService = new ExternalQuotesProvider();

    @Test
    public void convertReturnsMinBidIfLessThanFirstQuote() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1));
        Assert.assertEquals(BigDecimal.valueOf(1000), offer);
    }

    @Test
    public void convertReturnsMaxBidIfGreaterThanLastQuote() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1111));
        Assert.assertEquals(BigDecimal.valueOf(10000), offer);
    }

    @Test
    public void convertReturns2ndBidIfIn2ndQuote() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(11));
        Assert.assertEquals(BigDecimal.valueOf(2000), offer);
    }

    @Test
    public void convertReturns3rdBidIfEquals3rdQuoteVolumeSize() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(20));
        Assert.assertEquals(BigDecimal.valueOf(3000), offer);
    }
}