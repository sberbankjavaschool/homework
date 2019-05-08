package ru.sberbank.school.task02;

import org.junit.Test;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void volumeIsOverlap() {
        CalculatorFactory calculatorFactory = new CalculatorFactory();
        ExtendedFxConversionService extendedCalculator =
                calculatorFactory.getExtendedFxConversionService(new ExternalQuotesServiceDemo());

        assertNotNull(extendedCalculator);

        Optional<BigDecimal> price = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_000), Beneficiary.CLIENT);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(75), MathContext.DECIMAL32), price.get());

        price = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_500), Beneficiary.CLIENT);
        assertTrue(!price.isPresent());

        price = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_750_000), Beneficiary.CLIENT);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(77), MathContext.DECIMAL32), price.get());

        price = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_750_000), Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(78), MathContext.DECIMAL32), price.get());

        price = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(0.5), Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(75), MathContext.DECIMAL32), price.get());
        //assertTrue(extendedCalculator == null);

    }
}
