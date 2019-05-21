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
import static org.junit.Assert.assertEquals;

public class ExCalcTest {

    @Test
    public void exCalc() {
        CalculatorFactory calculatorFactory = new CalculatorFactory();
        ExtendedFxConversionService extendedCalculator =
                calculatorFactory.getExtendedFxConversionService(new ExternalQuotesServiceDemo());
        assertNotNull(extendedCalculator);

        Optional<BigDecimal> priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_000), Beneficiary.CLIENT);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(75), 10, RoundingMode.HALF_UP), priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_500), Beneficiary.CLIENT);
        assertTrue(!priceRub.isPresent());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_750_000), Beneficiary.CLIENT);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(77), 10, RoundingMode.HALF_UP), priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_750_000), Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(78), 10, RoundingMode.HALF_UP), priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(0.5), Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(75), 10, RoundingMode.HALF_UP), priceRub.get());

    }
}
