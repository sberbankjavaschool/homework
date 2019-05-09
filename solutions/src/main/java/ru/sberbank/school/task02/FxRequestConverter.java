package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;

public class FxRequestConverter {

    public static Symbol getSymbol(FxRequest request) {
        if (Symbol.RUB_USD.getSymbol().equals(request.getSymbol())) {
            return Symbol.RUB_USD;
        }
        if (Symbol.USD_RUB.getSymbol().equals(request.getSymbol())) {
            return Symbol.USD_RUB;
        }
        throw new WrongSymbolException("В запросе указана неверная валютная пара");
    }

    public static ClientOperation getDirection(FxRequest request) {
        try {
            return ClientOperation.valueOf(request.getDirection());
        } catch (IllegalArgumentException e) {
            throw new ConverterConfigurationException("В запросе указана неверная операция");
        } catch (NullPointerException e) {
            throw new ConverterConfigurationException("В запросе указана пустая операция");
        }
    }

    public static BigDecimal getAmount(FxRequest request) {
        BigDecimal amount;
        try {
            amount = BigDecimal.valueOf(Double.valueOf(request.getAmount()));
        } catch (NumberFormatException | NullPointerException e) {
            throw new ConverterConfigurationException("В запросе указано некорректное значение объема");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConverterConfigurationException("В запросе указано отрицательное значение объема");
        }
        return amount;
    }


}
