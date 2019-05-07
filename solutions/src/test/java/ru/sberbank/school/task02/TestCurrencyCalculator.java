package ru.sberbank.school.task02;
import org.junit.*;
import ru.sberbank.school.task02.calculator.CurrencyCalculator;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class TestCurrencyCalculator {
    FxConversionService conversionService;
    @Before
    public void initBefore() {
        conversionService = new CurrencyCalculator(new ExternalQuotesServiceDemo());

    }

    @Test
    public void FirstTest() {
        System.out.println(
                conversionService.convert(ClientOperation.BUY,
                        Symbol.USD_RUB,
                        BigDecimal.valueOf(900)
                        )
        );
    }
}
