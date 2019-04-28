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
        List<Quote> quotes = service.getQuotes(symbol);
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || quotes.size() <= 0) {
            return null;
        }

        quotes.sort((o1, o2) -> {
            if (o1.isInfinity()) {
                return 1;
            }
            if (o2.isInfinity()) {
                return -1;
            }
            return o1.getVolumeSize().compareTo(o2.getVolumeSize());
        });

        Quote matchedQuote = getMatchedQuote(quotes, amount);
        if (matchedQuote == null) {
            return null;
        }
        return operation == ClientOperation.SELL ? matchedQuote.getBid() : matchedQuote.getOffer();
    }

    private Quote getMatchedQuote(List<Quote> quotes, BigDecimal amount) {
        Quote res = null;
        for (Quote q : quotes) {
            if (amount.compareTo(q.getVolumeSize()) < 0) {
                res = q;
                break;
            }
        }

        if (res == null) {
            res = getInfQuote(quotes);
        }
        return res;
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
