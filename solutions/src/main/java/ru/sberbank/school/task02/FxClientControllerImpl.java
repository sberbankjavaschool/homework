package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class FxClientControllerImpl implements FxClientController {

    private FxConversionService fxConversionService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

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
        BigDecimal price = null;
        try {
            price = fxConversionService.convert(operation, symbol, amount);
        } catch (FxConversionException e) {
            //Log exception
        } finally {
            return new FxResponse(symbol.getSymbol(),
                    price != null ? price.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : null,
                    amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                    LocalDateTime.now().format(dateTimeFormatter),
                    operation.toString(),
                    price == null);
        }
    }
}