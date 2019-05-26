package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mart
 * 04.05.2019
 **/
public class FxConversionServiceImpl implements FxConversionService {
    private ExternalQuotesService quotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        this.quotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        Objects.requireNonNull(operation);
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(amount);

        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new IllegalArgumentException(
                    "Wrong operation input: amount is negative or zero");
        }

        List<Quote> quotes = quotesService.getQuotes(symbol);

        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("list of quotes is empty or null");
        }

        Quote correctQuote = quotes.stream()
                .filter(quote -> (amount.compareTo(quote.getVolumeSize()) < 0
                && amount.compareTo(BigDecimal.valueOf(0)) > 0) || quote.isInfinity())
                .min((q1, q2) -> {
                    if (q1.isInfinity()) {
                        return 1;
                    } else if (q2.isInfinity()) {
                        return -1;
                    } else {
                        return q1.getVolumeSize().compareTo(q2.getVolumeSize());
                    }
                })
                .orElse(quotes.get(quotes.size() - 1));

        return operation == ClientOperation.BUY ? correctQuote.getOffer() : correctQuote.getBid();
    }
}