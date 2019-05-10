package ru.sberbank.school.task02;

public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new SimpleFxConversionService(externalQuotesService);
    }
}
