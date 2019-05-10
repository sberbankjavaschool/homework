package ru.sberbank.school.task02;

import lombok.NonNull;

public class ServiceFactoryImpl implements ServiceFactory {

    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    public FxConversionService getFxConversionService(@NonNull ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);
    }

    public ExtendedFxConversionService getExtendedFxConversionService(@NonNull ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);
    }

}
