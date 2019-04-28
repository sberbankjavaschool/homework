package ru.sberbank.school.task02.services.implementation;

import ru.sberbank.school.task02.*;

public class ServiceFactoryImpl implements ServiceFactory {
    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}