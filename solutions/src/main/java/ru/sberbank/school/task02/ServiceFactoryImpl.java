package ru.sberbank.school.task02;

public class ServiceFactoryImpl implements ServiceFactory {

    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);
    }

    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceImpl(externalQuotesService);

    }

}
