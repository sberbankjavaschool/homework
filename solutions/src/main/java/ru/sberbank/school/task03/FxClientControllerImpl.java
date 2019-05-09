package ru.sberbank.school.task03;

import ru.sberbank.school.task02.*;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FxClientControllerImpl implements FxClientController {

    FxConversionService fxConversionService;

    public FxClientControllerImpl(FxConversionService fxConversionService) {
        this.fxConversionService = fxConversionService;
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {

        List<FxResponse> responses = new ArrayList<>();

        for (FxRequest r : requests) {
            responses.add(fetchResult(r));
        }

        return responses;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) throws FxConversionException {

        if (requests == null) {
            throw new FxConversionException("Некорректный запрос");
        }

        if (requests.getSymbol() == null) {
            throw new FxConversionException("Некорректный запрос в поле symbol");
        }
        String symbolAsString = requests.getSymbol();

        if (requests.getDirection() == null) {
            throw new FxConversionException("Некорректный запрос в поле direction");
        }
        String directionAsString = requests.getDirection();

        if (requests.getAmount() == null) {
            throw new FxConversionException("Некорректный запрос в поле amount");
        }
        String amountAsString = requests.getAmount();

        Symbol symbol;
        switch (symbolAsString) {
            case "USD/RUB":
                symbol = Symbol.USD_RUB;
                break;
            case "RUB/USD":
                symbol = Symbol.RUB_USD;
                break;
            default:
                symbol = null;
                throw new FxConversionException("Некорректная валюта");
        }

        ClientOperation operation;
        switch (directionAsString) {
            case "SELL":
                operation = ClientOperation.SELL;
                break;
            case "BUY":
                operation = ClientOperation.BUY;
                break;
            default:
                operation = null;
                throw new FxConversionException("Некорректная операция");
        }

        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountAsString));
        BigDecimal price = fxConversionService.convert(operation, symbol, amount);

        FxResponse fxResponse = new FxResponse(symbolAsString, String.valueOf(price.setScale(2)),
                String.valueOf(amount.setScale(2)), String.valueOf(LocalDate.now()), false);

        return fxResponse;
    }
}
