package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FxClientControllerImpl implements FxClientController {

    private FxConversionService fxConversionService;

    public FxClientControllerImpl(@NonNull ExternalQuotesService externalQuotesService) {
        fxConversionService = new FxConversionServiceImpl(externalQuotesService);
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
        try {
            BigDecimal answer = fxConversionService.convert(operation, symbol, amount);
            return new FxResponse(symbol.getSymbol(),
                    answer.setScale(2,BigDecimal.ROUND_HALF_UP).toString(),
                    amount.setScale(2,BigDecimal.ROUND_HALF_UP).toString(),
                    new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(new Date()),
                    operation.toString(),
                    false);
        } catch (FxConversionException e) {
            return new FxResponse(symbol.getSymbol(),
                    null,
                    amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                    new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(new Date()),
                    operation.toString(),
                    true);
        }
    }
}

