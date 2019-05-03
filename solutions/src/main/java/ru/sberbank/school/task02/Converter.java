package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.List;

public class Converter implements FxConversionService {

    private ExternalQuotesService quotesDatabase;

    public Converter(ExternalQuotesService externalQuotesService) {
        quotesDatabase = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = quotesDatabase.getQuotes(symbol);

        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("Price database not available");
        }

        if (amount.signum() <= 0) {
            throw new FxConversionException("Amount less than or equal to 0");
        }

        Quote quote = searchQuote(amount, quotes);

        return getPrice(operation, quote);
    }

    private BigDecimal getPrice (ClientOperation operation, Quote quote) {
        switch (operation) {
            case BUY:
                return quote.getOffer();
            case SELL:
                return quote.getBid();
            default:
                throw new FxConversionException("Incorrect operation");
        }
    }

    private Quote searchQuote(BigDecimal amount, List<Quote> quotes) {
        Quote searchQuote = null;
        Quote quoteInfinity = null;

        for (Quote quote : quotes) {
            if (quote.isInfinity()) {
                quoteInfinity = quote;
                continue;
            }
            if (amount.compareTo(quote.getVolumeSize()) < 0) {
                if (searchQuote == null) {
                    searchQuote = quote;
                } else if (searchQuote.getVolumeSize().compareTo(quote.getVolumeSize()) > 0) {
                    searchQuote = quote;
                }
            }
        }
        if (searchQuote == null) {
            searchQuote = quoteInfinity;
        }

        return searchQuote;
    }
}