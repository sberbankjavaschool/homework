package ru.sberbank.school.task02;

/**
 * Фабрика сервиса - валютного калькулятора.
 */
public interface ServiceFactory {
    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService);

    default ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        throw new UnsupportedOperationException();
    }
}
