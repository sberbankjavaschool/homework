package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;

public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        if (externalQuotesService == null) {
            throw new FxConversionException("Используемый сервис котировок не инициализирован (отсутствует)");
        }
        return new FxConversionServiceImpl(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        if (externalQuotesService == null) {
            throw new FxConversionException("Используемый сервис котировок не инициализирован (отсутствует)");
        }
        return null;
    }
}