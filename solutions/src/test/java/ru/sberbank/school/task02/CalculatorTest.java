package ru.sberbank.school.task02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

class CalculatorTest {
    private ServiceFactory factory = new ServiceFactoryImpl();
    private ExternalQuotesService quotesService = new ExternalQuotesProvider();
    private ExternalQuotesService demoQuotesService = new ExternalQuotesServiceDemo();

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
    void convertReturns4rdBidIfEquals3rdQuoteVolumeSize() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(30));
        Assertions.assertEquals(BigDecimal.valueOf(4000), offer);
    }

    @Test
    void convertReturnsIfQuoteVolumeSizeDouble() {
        FxConversionService calculator = factory.getFxConversionService(quotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(31.0234234));
        Assertions.assertEquals(BigDecimal.valueOf(4000), offer);
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

    @Test
    void convertDemoProviderMoreThanMaxValue() {
        FxConversionService calculator = factory.getFxConversionService(demoQuotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1_000_001));
        Assertions.assertEquals(BigDecimal.valueOf(84), offer);
    }

    @Test
    void convertDemoProviderMoreThan1000Double() {
        FxConversionService calculator = factory.getFxConversionService(demoQuotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1005.0410071948494));
        Assertions.assertEquals(BigDecimal.valueOf(82), offer);
    }

    @Test
    void convertDemoProviderMaxValue() {
        FxConversionService calculator = factory.getFxConversionService(demoQuotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1_000_000));
        Assertions.assertEquals(BigDecimal.valueOf(84), offer);
    }

    @Test
    void convertDemoProviderMinValue() {
        FxConversionService calculator = factory.getFxConversionService(demoQuotesService);
        BigDecimal offer = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(10));
        Assertions.assertEquals(BigDecimal.valueOf(85), offer);
    }
}