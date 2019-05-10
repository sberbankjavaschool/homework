package ru.sberbank.school.task02;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.NonNull;

import ru.sberbank.school.task02.exception.FxConversionException;
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
        ArrayList<FxResponse> responses = new ArrayList<>();
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

        boolean notFound = false;

        BigDecimal price;
        String priceResponse;
        try {
            price = conversionService.convert(clientOperation, symbol, amount);
            priceResponse = price.toString();
        } catch (FxConversionException e) {
            notFound = true;
            priceResponse = requests.getAmount();
        }

        Date date = new Date();

        response = FxResponse.builder()
                .amount(requests.getAmount())
                .symbol(requests.getSymbol())
                .price(priceResponse)
                .date(date.toString())
                .build();


        return response;
    }
}
