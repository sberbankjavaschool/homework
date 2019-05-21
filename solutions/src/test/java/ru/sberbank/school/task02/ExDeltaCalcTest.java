package ru.sberbank.school.task02;

import org.junit.Test;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExDeltaCalcTest {

    @Test
    public void exDeltaCalc() {

        //String beneficiary = System.getenv().get("SBRF_BENEFICIARY");

        CalculatorFactory calculatorFactory = new CalculatorFactory();
        ExtendedFxConversionService extendedCalculator = calculatorFactory
                .getExtendedFxConversionService(new ExternalQuotesServiceDemo());
        Optional<BigDecimal> priceRub;

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_750_000),0, Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(78), 10, RoundingMode.HALF_UP),
                priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(38_000), 0, Beneficiary.CLIENT);
        assertTrue(!priceRub.isPresent());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(38_200), 400, Beneficiary.CLIENT);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(76), 10, RoundingMode.HALF_UP),
                priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(38_250), 300, Beneficiary.CLIENT);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(77), 10, RoundingMode.HALF_UP),
                priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(7_550), 50, Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(76), 10, RoundingMode.HALF_UP),
                priceRub.get());

        priceRub = extendedCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                new BigDecimal(77_500), 600,  Beneficiary.BANK);
        assertEquals(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(78), 10, RoundingMode.HALF_UP),
                priceRub.get());
    }
}
