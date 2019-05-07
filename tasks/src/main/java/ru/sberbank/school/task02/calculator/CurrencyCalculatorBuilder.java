package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.ServiceFactory;

public class CurrencyCalculatorBuilder implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new CurrencyCalculator(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}
