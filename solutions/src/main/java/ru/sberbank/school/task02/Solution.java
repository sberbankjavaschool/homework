package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.Optional;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

public class Solution {
    public static void main(String[] args) {

        CalculatorFactory calculatorFactory = new CalculatorFactory();
        // получаем наш валютный калькулятор
        FxConversionService calculator =
                calculatorFactory.getFxConversionService(new ExternalQuotesServiceDemo());
        ExtendedFxConversionService extendedCalculator =
                calculatorFactory.getExtendedFxConversionService(new ExternalQuotesServiceDemo());

        Beneficiary beneficiary = System.getenv("Beneficiary") == "CLIENT"
                ? Beneficiary.CLIENT : Beneficiary.BANK;

        try {
            BigDecimal priceUsd = calculator.convert(ClientOperation.BUY,
                    Symbol.USD_RUB, new BigDecimal(100));
            System.out.println("USD: " + priceUsd);

            Optional<BigDecimal> priceRub = extendedCalculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                    new BigDecimal(100), beneficiary);
            if (priceRub.isPresent()) {
                System.out.println("RUB " + priceRub.get());
            }
            else {
                System.out.println("No reverseQuotes!");
            }
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        } catch (FxConversionException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
