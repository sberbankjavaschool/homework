package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FxClientControllerImpl implements FxClientController {
    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        List<FxResponse> result = new ArrayList<>();
        for (FxRequest request : requests) {
            result.add(fetchResult(request));
        }
        return result;
    }

    @Override
    public FxResponse fetchResult(FxRequest request) {
        ClientOperation operation = FxRequestConverter.getDirection(request);
        Symbol symbol = FxRequestConverter.getSymbol(request);
        BigDecimal amount = FxRequestConverter.getAmount(request);
        FxConversionService fxConversionService =
                new ServiceFactoryImpl().getFxConversionService(new ExternalQuotesServiceDemo());
        try {
            BigDecimal answer = fxConversionService.convert(operation, symbol, amount);
            return new FxResponse(symbol.getSymbol(), answer.toString(),
                    amount.toString(), new Date().toString(), false);
        } catch (FxConversionException ignore) {
            return new FxResponse(symbol.getSymbol(), "",
                    amount.toString(), new Date().toString(), true);
        }
    }
}
