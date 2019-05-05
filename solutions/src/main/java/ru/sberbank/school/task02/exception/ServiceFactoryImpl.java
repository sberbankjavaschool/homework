package ru.sberbank.school.task02.exception;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}
