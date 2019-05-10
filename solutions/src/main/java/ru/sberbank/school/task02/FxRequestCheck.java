package ru.sberbank.school.task02;

import lombok.NonNull;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;

import java.math.BigDecimal;

public class FxRequestCheck {
    public static ClientOperation getOperation(@NonNull FxRequest request) {
        try {
            return (ClientOperation.valueOf(request.getDirection().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new FxConversionException("Wrong operation");
        }
    }

    public static Symbol getSymbol(@NonNull FxRequest request) {
        if (request.getSymbol().toUpperCase().equals("USD/RUB")) {
            return Symbol.USD_RUB;
        } else if (request.getSymbol().toUpperCase().equals("RUB/USD")) {
            return Symbol.RUB_USD;
        }
        throw new WrongSymbolException("Wrong symbol");
    }

    public static BigDecimal getAmount(@NonNull FxRequest request) {
        if (BigDecimal.valueOf(Double.valueOf(request.getAmount())).equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Amount can't be equal to ZERO");
        }
        BigDecimal rightAmount;
        try {
            rightAmount = BigDecimal.valueOf(Double.valueOf(request.getAmount()));
        } catch (NumberFormatException ex) {
            throw new FxConversionException("Wrong amount");
        }
        return rightAmount;
    }

}

