package ru.sberbank.school.task02;

public class ServiceFactoryForCurrCalc implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new CurrencyCalculator(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return new ExtendedCurrencyCalc(externalQuotesService);
    }
}
