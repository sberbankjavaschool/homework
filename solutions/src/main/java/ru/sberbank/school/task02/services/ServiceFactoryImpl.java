package ru.sberbank.school.task02.services;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.ServiceFactory;
import ru.sberbank.school.task02.calculators.CurrencyCalc;
import ru.sberbank.school.task02.calculators.ExtendedCurrencyCalc;

public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new CurrencyCalc(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return new ExtendedCurrencyCalc(externalQuotesService);
    }
}
