package ru.sberbank.school.task02;

import java.math.BigDecimal;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

public class Solution {
    public static void main(String[] args) {

        CalculatorFactory calculatorFactory = new CalculatorFactory();
        // получаем наш валютный калькулятор
        FxConversionService calculator =
                calculatorFactory.getFxConversionService(new ExternalQuotesServiceDemo());

        try {
            BigDecimal price = calculator.convert(ClientOperation.BUY,
                    Symbol.USD_RUB, new BigDecimal(100));
            System.out.println(price);
        } catch (WrongSymbolException ex) {
            System.out.println(ex.getMessage());
        } catch (ConverterConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
