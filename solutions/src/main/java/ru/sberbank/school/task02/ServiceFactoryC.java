package ru.sberbank.school.task02;

public class ServiceFactoryC implements ServiceFactory {

    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new FxConversionServiceC(externalQuotesService);
    }

}
