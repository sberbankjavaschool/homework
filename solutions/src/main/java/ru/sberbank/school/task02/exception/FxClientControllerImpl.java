package ru.sberbank.school.task02.exception;

import lombok.NonNull;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxClientController;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FxClientControllerImpl implements FxClientController {

    private FxConversionServiceImpl fxConversionServiceImpl;

    public FxClientControllerImpl(@NonNull ExternalQuotesService externalQuotesService) {
        fxConversionServiceImpl = new FxConversionServiceImpl(externalQuotesService);
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        List<FxResponse> responses = new ArrayList<>();
        for (FxRequest request : requests) {
            responses.add(fetchResult(request));
        }
        return responses;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        FxResponse response;

        Symbol symbol = getSymbol(requests.getSymbol());
        ClientOperation clientOperation = getDirection(requests.getDirection());
        BigDecimal amount = getAmount(requests.getAmount());
        BigDecimal price = fxConversionServiceImpl.convert(clientOperation, symbol, amount);

        if(price != null) {
            return response = new FxResponse(symbol.getSymbol(),
                price.toString(),
                amount.toString(),
                LocalDateTime.now().toString(),
                clientOperation.toString(),
                false);
        } else {
            return response = new FxResponse(symbol.getSymbol(),
                null,
                amount.toString(),
                LocalDateTime.now().toString(),
                clientOperation.toString(),
                true);
        }

    }

    public static Symbol getSymbol(@NonNull String symbol) {
        if (Symbol.RUB_USD.getSymbol().equalsIgnoreCase(symbol)) {
            return Symbol.RUB_USD;
        }
        if (Symbol.USD_RUB.getSymbol().equalsIgnoreCase(symbol)) {
            return Symbol.USD_RUB;
        }
        throw new WrongSymbolException("Неизвестная валютная пара");
    }

    public static ClientOperation getDirection(@NonNull String direction) {
        ClientOperation operation = null;
        try {
            operation = ClientOperation.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FxConversionException("Неизвестная операция");
        }
        return operation;
    }

    public static BigDecimal getAmount(@NonNull String amount) {
        BigDecimal amountParse = null;
        try {
            amountParse = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new FxConversionException("Некорректное значение amount");
        }
        if (amountParse.compareTo(BigDecimal.ZERO) <= 0) {
            throw new FxConversionException("Amount меньше или равно нулю");
        }
        return amountParse;
    }

}
