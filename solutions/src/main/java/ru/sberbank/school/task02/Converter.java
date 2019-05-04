package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {
    private ExternalQuotesService externalQuotesService;

    public Converter(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null || symbol == null || amount == null) {
            throw new NullPointerException();
        }

        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Введите корректное значение объема!");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes.isEmpty()) {
            throw new FxConversionException("Список валют пуст!");
        }

        Quote result = getRightQuote(quotes,amount);

        boolean isSell = operation == ClientOperation.SELL;
        return isSell ? result.getBid() : result.getOffer();
    }

    private Quote getRightQuote(List<Quote> quotes,BigDecimal amount) {
        Quote result = null;
        for (Quote q : quotes) {
            BigDecimal quoteValueSize = q.getVolumeSize();
            if (amount.compareTo(quoteValueSize) < 0) {
                if (q.isInfinity()) {
                    result = q;
                    continue;
                }
                if (result == null || result.getVolumeSize().compareTo(quoteValueSize) > 0) {
                    result = q;
                }
            }
        }

        return result != null ? result : quotes.get(0);
    }


}
