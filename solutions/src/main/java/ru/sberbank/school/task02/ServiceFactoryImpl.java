package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;

public class ServiceFactoryImpl implements ServiceFactory {

    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService)
            throws FxConversionException {

        if (externalQuotesService == null) {
            throw new FxConversionException("Неправильная ссылка на объект котировок ExternalQuotesService");
        } else {
            //Инстанс объекта FxConversionService в который передается ExternalQuotesService
            FxConversionService fxConversionService = new FxConversionServiceImpl(externalQuotesService);
            return fxConversionService;
        }
    }
}
