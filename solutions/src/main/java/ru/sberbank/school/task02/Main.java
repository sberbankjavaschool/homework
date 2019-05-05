package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        CurrencyCalcFactory factory = new CurrencyCalcFactory();

        FxConversionService calculator =
                factory.getFxConversionService(new ExternalQuotesServiceDemo());

        BigDecimal price = calculator.convert(ClientOperation.BUY,
                Symbol.USD_RUB, BigDecimal.valueOf(1000));
        System.out.println(price);
    }
}