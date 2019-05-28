package ru.sberbank.school.task02;

public class CurrencyCalcFactory implements ServiceFactory {


    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new CurrencyCalc(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return new ReversedCurrencyCalc(externalQuotesService);
    }
}

