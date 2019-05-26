package ru.sberbank.school.task02.services.implementation;

import lombok.NonNull;
import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.ServiceFactory;
import ru.sberbank.school.task02.services.InternalQuotesService;

/**
 * Фабрика сервиса - валютного калькулятора.
 */
public class ServiceFactoryImpl implements ServiceFactory {

    /**
     * Возвращает инстанс FxConversionService реализованнную студентом.
     *
     * @param externalQuotesService Сервис-источник актуальных котировок
     * @return объект - валютный калькулятор
     */
    @Override
    public FxConversionService getFxConversionService(@NonNull ExternalQuotesService externalQuotesService) {
        InternalQuotesService internalQuotesService = new InternalQuotesServiceImpl(externalQuotesService);
        return new FxConversionServiceImpl(internalQuotesService);
    }

    /**
     * Возвращает инстанс ExtendedFxConversionService.
     *
     * @param externalService Сервис-источник актуальных котировок
     * @return объект - обратный валютный калькулятор
     */
    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(@NonNull ExternalQuotesService externalService) {
        InternalQuotesService internalQuotesService = new InternalQuotesServiceImpl(externalService);
        return new ExtendedFxConversionServiceImpl(internalQuotesService);
    }
}