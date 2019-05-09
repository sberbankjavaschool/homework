package ru.sberbank.school.task02;

import lombok.NonNull;

public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(@NonNull ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(
            @NonNull ExternalQuotesService externalQuotesService) {
        return null;
    }
}