package ru.sberbank.school.task02.util;

import java.math.BigDecimal;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.WrongSymbolException;


public class RequesParser {

    public static Symbol getSymbol(@NonNull String symbol) {

        if (Symbol.RUB_USD.getSymbol().equals(symbol)) {
            return Symbol.RUB_USD;
        }
        if (Symbol.USD_RUB.getSymbol().equals(symbol)) {
            return Symbol.USD_RUB;
        }
        throw new WrongSymbolException("Указана неверная валютна пара!");
    }

    public static ClientOperation getClientOperetion(@NonNull String clientOperetion) {
        try {
            return ClientOperation.valueOf(clientOperetion);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Вы ввели некорректную операцию!");
        }
    }

    public static BigDecimal getAmount(@NonNull String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Вы ввели некорректный объем!");
        }
    }

}
