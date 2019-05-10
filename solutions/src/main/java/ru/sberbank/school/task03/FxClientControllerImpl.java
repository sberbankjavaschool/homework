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
    FxRequestConversionService fxRequestConversionService;

    public FxClientControllerImpl(FxConversionService fxConversionService,
                                  FxRequestConversionService fxRequestConversionService) {

        this.fxConversionService = fxConversionService;

        this.fxRequestConversionService = fxRequestConversionService;
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

        String symbolAsString = requests.getSymbol();
        Symbol symbol = fxRequestConversionService.getSymbol(symbolAsString);

        String directionAsString = requests.getDirection();
        ClientOperation operation = fxRequestConversionService.getClientOperation(directionAsString);


        if (requests.getAmount() == null) {
            throw new FxConversionException("Некорректный запрос в поле amount");
        }
        String amountAsString = requests.getAmount();

        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountAsString));
        BigDecimal price = fxConversionService.convert(operation, symbol, amount);

        FxResponse fxResponse = new FxResponse(symbolAsString, String.valueOf(price.setScale(2)),
                String.valueOf(amount.setScale(2)), String.valueOf(LocalDate.now()), false);

        return fxResponse;
    }
}
