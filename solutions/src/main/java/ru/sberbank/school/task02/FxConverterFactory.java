package ru.sberbank.school.task02;

public class FxConverterFactory implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService)
            throws IllegalArgumentException, NullPointerException {
        return new FxConverter(externalQuotesService);
    }
}
