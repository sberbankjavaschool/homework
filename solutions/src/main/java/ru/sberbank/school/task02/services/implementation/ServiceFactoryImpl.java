package ru.sberbank.school.task02.services.implementation;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.ServiceFactory;
import ru.sberbank.school.task02.services.InternalQuotesService;

public class ServiceFactoryImpl implements ServiceFactory {
    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        InternalQuotesService internalQuotesService = new InternalQuotesServiceImpl(externalQuotesService);
        return new FxConversionServiceImpl(internalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}