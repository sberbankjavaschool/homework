package ru.sberbank.school.task02;

import java.math.BigDecimal;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

public class Main {
    public static void main(String[] args) {

        FxConversionService calculator =
                new CurrencyCalcFactory().getFxConversionService(new ExternalQuotesServiceDemo());

        BigDecimal price = calculator.convert(ClientOperation.BUY,
                Symbol.USD_RUB, BigDecimal.valueOf(1000));
        System.out.println(price);
    }
}