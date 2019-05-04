package ru.sberbank.school.task02;

public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {

        FxConversionService fxConversionService = new FxConversionServiceImpl();
        return fxConversionService;
    }
}
