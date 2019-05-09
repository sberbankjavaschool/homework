package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.util.Symbol;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FxClientControllerImpl implements FxClientController {

    private ExternalQuotesService externalQuotesService;

    public FxClientControllerImpl(@NonNull ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public List<FxResponse> fetchResult(@NonNull List<FxRequest> requests) {
        List<FxResponse> result = new ArrayList<>();
        for (FxRequest request : requests) {
            result.add(fetchResult(request));
        }
        return result;
    }

    @Override
    public FxResponse fetchResult(@NonNull FxRequest request) {
        ClientOperation operation = FxRequestConverter.getDirection(request);
        Symbol symbol = FxRequestConverter.getSymbol(request);
        BigDecimal amount = FxRequestConverter.getAmount(request);
        FxConversionService fxConversionService =
                new ServiceFactoryImpl().getFxConversionService(externalQuotesService);
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
