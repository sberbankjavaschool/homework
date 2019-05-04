package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

import static ru.sberbank.school.task02.util.Symbol.USD_RUB;

import java.math.BigDecimal;


public class Main {

    public static void main(String[] args) {

        //Создание объекта со списком котировок
        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();

        //Создание объекта валютного калькулятора FxConversionService с помощью ServiceFactory
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        FxConversionService fxConversionService = serviceFactory.getFxConversionService(externalQuotesService);

        //Получение котировки
        ClientOperation testClientOperation = ClientOperation.BUY;
        Symbol testSymbol = USD_RUB;
        BigDecimal testValue = BigDecimal.valueOf(1000);

        BigDecimal testPrice = fxConversionService.convert(testClientOperation, testSymbol, testValue);
        System.out.println(testPrice);




    }

}
