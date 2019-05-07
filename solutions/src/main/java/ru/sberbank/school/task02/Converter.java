package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import lombok.NonNull;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {

    private ExternalQuotesService service;

    public Converter(ExternalQuotesService service) {
        this.service = service;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation, @NonNull Symbol symbol, @NonNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        List<Quote> quotes = service.getQuotes(symbol);

        Quote matchedQuote = getMatchedQuote(quotes, amount);

        return getPrice(operation, matchedQuote);
    }

    private BigDecimal getPrice(ClientOperation operation, Quote matchedQuote) {
        if (matchedQuote == null) {
            return null;
        }
        return operation == ClientOperation.SELL ? matchedQuote.getBid() : matchedQuote.getOffer();
    }

    private Quote getMatchedQuote(List<Quote> quotes, BigDecimal amount) {
        if (quotes.size() <= 0) {
            return null;
        }

        Quote res = null;
        Quote infQuote = null;

        for (Quote q : quotes) {
            if (q.isInfinity()) {
                infQuote = q;
                continue;
            }
            if (((res == null) || quoteIsLowerThan(q, res)) && quoteIsMatch(q, amount)) {
                res = q;
            }
        }

        return res == null ? infQuote : res;
    }

    private boolean quoteIsMatch(Quote quote, BigDecimal amount) {
        return (quote.getVolumeSize().compareTo(amount) > 0);
    }

    private boolean quoteIsLowerThan(Quote first, Quote second) {
        return (first.getVolumeSize().compareTo(second.getVolumeSize()) < 0);
    }
}
