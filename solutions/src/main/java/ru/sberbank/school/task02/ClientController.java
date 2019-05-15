package ru.sberbank.school.task02;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

import ru.sberbank.school.task02.util.*;

public class ClientController implements FxClientController {

    private ExtendedConverterService conversionService;

    ClientController(ExternalQuotesService externalQuotesService) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        conversionService = (ExtendedConverterService) serviceFactory
                .getExtendedFxConversionService(externalQuotesService);
    }

    @Override
    public List<FxResponse> fetchResult(@NonNull List<FxRequest> requests) {
        List<FxResponse> responses = new ArrayList<>();
        for (FxRequest request : requests) {
            FxResponse response = fetchResult(request);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public FxResponse fetchResult(@NonNull FxRequest requests) {

        BigDecimal amount = RequestParser.getAmount(requests.getAmount());
        ClientOperation clientOperation = RequestParser.getClientOperation(requests.getDirection());
        Symbol symbol = RequestParser.getSymbol(requests.getSymbol());

        BigDecimal price = conversionService.convert(clientOperation, symbol, amount);
        String priceResponse = String.valueOf(price);
        String date = CurrentDate.getDate();

        return new FxResponse(symbol.getSymbol(), priceResponse,
                String.valueOf(amount), date, String.valueOf(clientOperation), false);
    }

}
