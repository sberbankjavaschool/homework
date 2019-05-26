package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Mart
 * 10.05.2019
 **/
public class FxRequestConverter {

    public static ClientOperation getClientOperation(FxRequest request) {
        return ClientOperation.valueOf(request.getDirection());
    }

    public static Symbol getSymbol(FxRequest request) {
        Objects.requireNonNull(request);
        Symbol symbol;
        switch (request.getSymbol()) {
            case "USD/RUB":
                symbol = Symbol.USD_RUB;
                break;
            case "RUB/USD":
                symbol = Symbol.RUB_USD;
                break;
            default:
                throw new IllegalArgumentException("Wrong input symbol in request:"
                        + request.getSymbol() + " expected \"BUY\" or \"SELL\"");
        }
        return symbol;
    }

    public static BigDecimal getAmount(FxRequest request) {
        Objects.requireNonNull(request);
        BigDecimal amount;
        try {
            amount = new BigDecimal(request.getAmount());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Wrong input amount of money:"
                        + request.getAmount() + " expected amount > 0");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong input amount of money:"
                    + request.getAmount() + " expected some number");
        }
    }
}
