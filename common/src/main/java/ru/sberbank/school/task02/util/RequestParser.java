package ru.sberbank.school.task02.util;

import java.math.BigDecimal;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.WrongSymbolException;

import static java.math.RoundingMode.HALF_UP;


public class RequestParser {

    public static final int SCALE = 6;

    public static Symbol getSymbol(@NonNull String symbol) {

        if (Symbol.RUB_USD.getSymbol().equals(symbol)) {
            return Symbol.RUB_USD;
        }
        if (Symbol.USD_RUB.getSymbol().equals(symbol)) {
            return Symbol.USD_RUB;
        }
        throw new WrongSymbolException("Указана неверная валютна пара!");
    }

    public static ClientOperation getClientOperation(@NonNull String clientOperetion) {
        return ClientOperation.valueOf(clientOperetion);
    }

    public static BigDecimal getAmount(@NonNull String amount) {
        return new BigDecimal(amount);
    }

    public static BigDecimal getPrice(@NonNull String priceStr) {
        return new BigDecimal(priceStr).setScale(SCALE, HALF_UP);
    }

}
