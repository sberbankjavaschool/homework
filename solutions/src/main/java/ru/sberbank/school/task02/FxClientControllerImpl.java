package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FxClientControllerImpl implements FxClientController {
    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        if (requests == null) {
            throw new ConverterConfigurationException("Передан пустой список запросов");
        }
        List<FxResponse> result = new ArrayList<>();
        for (FxRequest request : requests) {
            result.add(fetchResult(request));
        }
        return result;
    }

    @Override
    public FxResponse fetchResult(FxRequest request) {
        if (request == null) {
            throw new ConverterConfigurationException("Передан пустой запрос");
        }
        ClientOperation operation = FxRequestConverter.getDirection(request);
        Symbol symbol = FxRequestConverter.getSymbol(request);
        BigDecimal amount = FxRequestConverter.getAmount(request);
        FxConversionService fxConversionService =
                new ServiceFactoryImpl().getFxConversionService(new ExternalQuotesServiceDemo());
        try {
            BigDecimal answer = fxConversionService.convert(operation, symbol, amount);
            return new FxResponse(symbol.getSymbol(), answer.toString(),
                    amount.toString(), new Date().toString(), false);
        } catch (FxConversionException e) {
            return new FxResponse(symbol.getSymbol(), null,
                    amount.toString(), new Date().toString(), true);
        }
    }
}
