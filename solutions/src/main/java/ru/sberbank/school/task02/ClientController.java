package ru.sberbank.school.task02;

import java.math.BigDecimal;

import java.util.LinkedList;
import java.util.List;

import lombok.NonNull;

import ru.sberbank.school.task02.util.*;

import static java.math.RoundingMode.HALF_UP;

public class ClientController implements FxClientController {

    private static final int SCALE = 8;

    private ExtendedConverterService conversionService;

    ClientController(ExternalQuotesService externalQuotesService) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        conversionService = (ExtendedConverterService) serviceFactory
                .getExtendedFxConversionService(externalQuotesService);
    }

    @Override
    public List<FxResponse> fetchResult(@NonNull List<FxRequest> requests) {
        List<FxResponse> responses = new LinkedList<>();
        for (FxRequest request : requests) {
            FxResponse response = fetchResult(request);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public FxResponse fetchResult(@NonNull FxRequest requests) {
        FxResponse response;

        BigDecimal amount = RequesParser.getAmount(requests.getAmount());
        ClientOperation clientOperation = RequesParser.getClientOperetion(requests.getDirection());
        Symbol symbol = RequesParser.getSymbol(requests.getSymbol());

        BigDecimal price = conversionService.convert(clientOperation, symbol, amount);
        String priceResponse = String.valueOf(price.setScale(SCALE, HALF_UP));
        String date = RequesParser.getDate();

        response = new FxResponse(symbol.getSymbol(), priceResponse, amount.toString(), date, false);

        return response;
    }
}
