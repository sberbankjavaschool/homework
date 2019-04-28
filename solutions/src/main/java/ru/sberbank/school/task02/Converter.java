package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {

    private ExternalQuotesService service;

    public Converter(ExternalQuotesService service) {
        this.service = service;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = service.getQuotes(symbol);
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || quotes.size() <= 0) {
            return null;
        }

        quotes.sort((o1, o2) -> {
            if (o1.isInfinity()) {
                return 1;
            }
            if (o2.isInfinity()) {
                return 0;
            }
            return o2.getVolumeSize().compareTo(o1.getVolumeSize());
        });

        Quote matchedQuote = getMatchedQuote(quotes, amount);
        if (matchedQuote == null) {
            return null;
        }
        return operation == ClientOperation.SELL ? matchedQuote.getBid() : matchedQuote.getOffer();
    }

    private Quote getMatchedQuote(List<Quote> quotes, BigDecimal amount) {
        int matchedIndex = -1;
        for (Quote q : quotes) {
            if (amount.compareTo(q.getVolumeSize()) > 0) {
                matchedIndex = quotes.indexOf(q);
            }
        }
        if (matchedIndex == -1 && getInfQuote(quotes) != null) {
            return getInfQuote(quotes);
        }
        if (matchedIndex < 0) {
            return null;
        }
        return quotes.get(matchedIndex);
    }

    private Quote getInfQuote(List<Quote> quotes) {
        for (Quote q : quotes) {
            if (q.isInfinity()) {
                return q;
            }
        }
        return null;
    }
}
