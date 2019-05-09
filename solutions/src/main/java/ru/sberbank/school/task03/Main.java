package ru.sberbank.school.task03;

import ru.sberbank.school.task02.*;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;

import static ru.sberbank.school.task02.util.Symbol.USD_RUB;

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) throw new FxConversionException("Неверное число аргументов");

        String symbol = args[0];
        String direction = args[1];
        String amount = args[2];

        //Создание объекта со списком котировок с конкретной реализацией ExternalQuotesServiceDemo
        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();

        //Создание объекта валютного калькулятора FxConversionService с помощью ServiceFactory
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        FxConversionService fxConversionService = serviceFactory.getFxConversionService(externalQuotesService);

        //Создание контроллера со ссылкой на fxConversionService
        FxClientController fxClientController = new FxClientControllerImpl(fxConversionService);


        //Создание объекта FxRequest и получение объекта FxResponse
        FxRequest testFxRequest = new FxRequest(symbol, direction, amount);
        FxResponse testFxResponse = fxClientController.fetchResult(testFxRequest);;

        System.out.println(testFxRequest);
        System.out.println(testFxResponse);

    }
}
