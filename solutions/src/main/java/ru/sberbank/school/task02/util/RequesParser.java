package ru.sberbank.school.task02.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.WrongSymbolException;


public class RequesParser {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
        return ClientOperation.valueOf(clientOperetion);
    }

    public static BigDecimal getAmount(@NonNull String amount) {
        return new BigDecimal(amount);
    }

    public static String getDate() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(timeFormatter);
    }

}
