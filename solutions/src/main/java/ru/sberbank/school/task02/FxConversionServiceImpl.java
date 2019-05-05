package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.exception.FxConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class FxConversionServiceImpl implements FxConversionService {

    ExternalQuotesService externalQuotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount)
            throws FxConversionException {

        if (amount.compareTo(BigDecimal.valueOf(0)) < 0 ) {
            throw new FxConversionException("Объем не может быть отрицательным");
        }

        List<Quote> quotes = new ArrayList<>();
        quotes = externalQuotesService.getQuotes(symbol);

        if (quotes.size() == 0) {
            throw new FxConversionException("Список котировок ExternalQuotesService не должен быть пустой");
        }

        int negativeValueCounter = 0;
        for (int i = 0; i < quotes.size(); i++) {
            if (quotes.get(i).getVolumeSize().compareTo(BigDecimal.valueOf(0)) < 0) {
                negativeValueCounter++;
            }
        }
        if (negativeValueCounter == quotes.size()) {
            throw new FxConversionException("Список котировок ExternalQuotesService содержит только отрицательные value");
        }

        //Создание списка котировок значение которых больше, чем amount
        List<Quote> quotesBiggerThanAmount = new ArrayList<>();
        for (int i = 0; i < quotes.size(); i++) {

            if (quotes.get(i).getVolumeSize().compareTo(amount) > 0
                    && quotes.get(i).getVolumeSize().compareTo(BigDecimal.valueOf(0)) > 0) {
                quotesBiggerThanAmount.add(quotes.get(i));
            }
        }

        //Если amount больше самого большого value, то вернуть котировку для минимального value больше 0 из quotes
        if (quotesBiggerThanAmount.size() == 0) {
            BigDecimal minValue = quotes.get(0).getVolumeSize();
            int minValueIndex = 0;
            for (int i = 0; i < quotes.size(); i++) {
                if (quotes.get(i).getVolumeSize().compareTo(minValue) < 0
                        && quotes.get(i).getVolumeSize().compareTo(BigDecimal.valueOf(0)) >0 ) {

                    minValue = quotes.get(i).getVolumeSize();
                    minValueIndex = i;
                }
            }

            if (operation == ClientOperation.BUY) {
                return quotes.get(minValueIndex).getOffer();
            } else if (operation == ClientOperation.SELL) {
                return quotes.get(minValueIndex).getBid();
            } else {
                throw new FxConversionException("Недопустимая операция ClientOperation");
            }

        //Выбрать минимальную котировку больше чем amount из quotesBiggerThanAmount
        } else {

            BigDecimal minValueBiggerThanAmount = quotesBiggerThanAmount.get(0).getVolumeSize();
            int minValueBiggerThanAmountIndex = 0;

            for (int i = 0; i < quotesBiggerThanAmount.size(); i++) {

                if (quotesBiggerThanAmount.get(i).getVolumeSize().compareTo(minValueBiggerThanAmount) < 0) {

                    minValueBiggerThanAmount = quotesBiggerThanAmount.get(i).getVolumeSize();
                    minValueBiggerThanAmountIndex = i;
                }
            }

            if (operation == ClientOperation.BUY) {
                return quotesBiggerThanAmount.get(minValueBiggerThanAmountIndex).getOffer();
            } else if (operation == ClientOperation.SELL) {
                return quotesBiggerThanAmount.get(minValueBiggerThanAmountIndex).getBid();
            } else {
                throw new FxConversionException("Недопустимая операция ClientOperation");
            }
        }
    }
}
