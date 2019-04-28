package ru.sberbank.school.task02.services.implementation;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {
    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}