package ru.sberbank.school.task02;

public class CalculatorFactory implements ServiceFactory {

    public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
        return new Calculator(externalQuotesService);
    }

}
