package ru.sberbank.school.task02;

import ru.sberbank.school.task02.controllers.FxClientControllerImpl;
import ru.sberbank.school.task02.services.implementation.ExternalQuotesServiceImpl;
import ru.sberbank.school.task02.services.implementation.ServiceFactoryImpl;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxRequestBuilder;
import ru.sberbank.school.task02.util.FxResponse;

import java.util.List;

/**
 * Created by Gregory Melnikov at 03.05.2019
 */
public class Main {
    
    public static void main(String[] args) {

        FxRequestBuilder requestBuilder = new FxRequestBuilder();

        List<FxRequest> requests = requestBuilder.buildRequests(args);

        for (FxRequest request : requests) {
            System.out.println(request.toString());
        }

        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceImpl();
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExtendedFxConversionService conversionService =
                serviceFactory.getExtendedFxConversionService(externalQuotesService);
        FxClientController clientController = new FxClientControllerImpl(conversionService);

        List<FxResponse> responses = clientController.fetchResult(requests);

        for (FxResponse response : responses) {
            System.out.println("response: " + response.toString() + ", price: " + response.getPrice());
        }
    }
}
