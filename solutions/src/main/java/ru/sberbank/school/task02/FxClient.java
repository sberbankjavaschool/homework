package ru.sberbank.school.task02;

import ru.sberbank.school.task02.controllers.FxClientControllerImpl;
import ru.sberbank.school.task02.services.implementation.ExternalQuotesServiceImpl;
import ru.sberbank.school.task02.services.implementation.ServiceFactoryImpl;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxRequestBuilder;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task03.util.ResponseFormatterImpl;

import java.util.List;

/**
 * Клиент для получения расчетных курсов из командной строки (args)
 * Формат ввода аргументов командной строки: USD/RUB BUY 3359156.48 USD/RUB SELL 87913.92
 * Created by Gregory Melnikov at 11.05.2019
 */
public class FxClient {

    public static void main(String[] args) {

        FxRequestBuilder requestBuilder = new FxRequestBuilder();

        List<FxRequest> requests = requestBuilder.buildRequests(args);

        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceImpl();
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExtendedFxConversionService conversionService =
                serviceFactory.getExtendedFxConversionService(externalQuotesService);
        FxClientController clientController = new FxClientControllerImpl(conversionService);

        try {
            List<FxResponse> responses = clientController.fetchResult(requests);

            ResponseFormatterImpl formatter = new ResponseFormatterImpl();
            System.out.println(formatter.format(responses));

        } catch (RuntimeException e) {
            System.out.println("Some trouble have arise while conversion");
            System.err.println(e.getMessage());
        }
    }
}
