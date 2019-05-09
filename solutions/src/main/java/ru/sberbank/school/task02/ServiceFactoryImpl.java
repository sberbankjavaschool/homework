package ru.sberbank.school.task02;

<<<<<<< HEAD
import ru.sberbank.school.task02.exception.FxConversionException;

public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        if (externalQuotesService == null) {
            throw new FxConversionException("Используемый сервис котировок не инициализирован (отсутствует)");
        }
=======
public class ServiceFactoryImpl implements ServiceFactory {
    @Override
    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
>>>>>>> d546d2fd06db1cff06faa4d90977720619d88ceb
        return new FxConversionServiceImpl(externalQuotesService);
    }

    @Override
    public ExtendedFxConversionService getExtendedFxConversionService(ExternalQuotesService externalQuotesService) {
        return null;
    }
}
