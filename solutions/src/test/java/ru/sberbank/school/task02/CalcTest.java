package ru.sberbank.school.task02;

import org.junit.Test;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

import static org.junit.Assert.*;

public class CalcTest {

    @Test
    public void calc() {
        CalculatorFactory calculatorFactory = new CalculatorFactory();

        FxConversionService calculator =
                calculatorFactory.getFxConversionService(new ExternalQuotesServiceDemo());
        assertNotNull(calculator);

        BigDecimal priceUsd = calculator.convert(ClientOperation.BUY,
                Symbol.USD_RUB, new BigDecimal(1_000));
        assertEquals(BigDecimal.valueOf(82), priceUsd);

        priceUsd = calculator.convert(ClientOperation.BUY,
                Symbol.USD_RUB, new BigDecimal(700));
        assertEquals(BigDecimal.valueOf(83), priceUsd);

        priceUsd = calculator.convert(ClientOperation.BUY,
                Symbol.USD_RUB, new BigDecimal(1_000_000));
        assertEquals(BigDecimal.valueOf(84), priceUsd);

    }
}
