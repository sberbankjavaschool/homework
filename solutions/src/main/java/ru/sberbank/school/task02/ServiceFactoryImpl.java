package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.util.ArrayList;
import java.util.List;


public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {

        //Список котировок типа ExternalQuotesService
        List<Quote> quotes = new ArrayList<>();
        quotes = externalQuotesService.getQuotes(Symbol.USD_RUB);

        //Инстанс объекта FxConversionService
        FxConversionService fxConversionService = new FxConversionServiceImpl(quotes);
        return fxConversionService;

    }
}
