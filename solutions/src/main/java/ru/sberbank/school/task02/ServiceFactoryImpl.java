package ru.sberbank.school.task02;

public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new Converter(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}