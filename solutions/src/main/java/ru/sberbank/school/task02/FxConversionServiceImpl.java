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

        if (amount.compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new FxConversionException("Объем не может быть отрицательным");
        }

        List<Quote> quotes = new ArrayList<>();
        quotes = externalQuotesService.getQuotes(symbol);

        if (quotes.size() == 0) {
            throw new FxConversionException("Список котировок ExternalQuotesService не должен быть пустой");
        }

        double amountAsDouble = amount.doubleValue();
        double tempValue = Double.MAX_VALUE;
        int index = 0;

        for (Quote q : quotes) {

            double value = q.getVolumeSize().doubleValue();

            if (value > 0) {
                if (value >= amountAsDouble) {
                    if (value < tempValue) {
                        tempValue = value;
                        index++;
                    }
                }
            }
        }

        if (operation == ClientOperation.BUY) {
            return quotes.get(index).getOffer();
        } else if (operation == ClientOperation.SELL) {
            return quotes.get(index).getBid();
        } else {
            throw new FxConversionException("Недопустимая операция ClientOperation");
        }
    }
}