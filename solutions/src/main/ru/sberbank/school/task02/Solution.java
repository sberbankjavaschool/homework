package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class Solution {
    public static void main(String[] args) {

        CalculatorFactory calculatorFactory = new CalculatorFactory();
        // получаем наш валютный калькулятор
        FxConversionService calculator = calculatorFactory.getFxConversionService(new ExternalQuotesServiceDemo());
        //TODO добавить ввод с консоли ?
        //System.out.println(Arrays.toString(ClientOperation.values()));
        try {
            BigDecimal price = calculator.convert(ClientOperation.BUY, Symbol.USD_RUB, new BigDecimal(10));
            System.out.println(price);
        }
        catch (WrongSymbolException ex) {
            System.out.println(ex.getMessage());
        }
        catch (ConverterConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
