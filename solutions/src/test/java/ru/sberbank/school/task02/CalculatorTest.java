package ru.sberbank.school.task02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

class CalculatorTest {
    private ServiceFactory factory = new ServiceFactoryImpl();
    private ExternalQuotesService quotesService = new ExternalQuotesProvider();

    @Test
    void convertReturnsMinBidIfLessThanFirstQuote() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1));
        Assertions.assertEquals(BigDecimal.valueOf(1000), offer);
    }

    @Test
    void convertReturnsMaxBidIfGreaterThanLastQuote() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(111111));
        Assertions.assertEquals(BigDecimal.valueOf(10000), offer);
    }

    @Test
    void convertReturns2ndBidIfIn2ndQuote() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(11));
        Assertions.assertEquals(BigDecimal.valueOf(2000), offer);
    }

    @Test
    void convertReturns3rdBidIfEquals3rdQuoteVolumeSize() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(30));
        Assertions.assertEquals(BigDecimal.valueOf(3000), offer);
    }

    @Test
    void convertIfProvidedNegativeAmount() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(-1));
        Assertions.assertNull(offer);
    }

    @Test
    void convertIfProvidedZeroAmount() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.ZERO);
        Assertions.assertNull(offer);
    }
}