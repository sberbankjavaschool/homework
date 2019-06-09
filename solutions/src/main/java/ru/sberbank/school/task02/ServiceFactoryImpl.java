package ru.sberbank.school.task02;

import ru.sberbank.school.task02.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new ConversionService(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return new ExtendedConversionService(externalQuotesService);
    }
}
