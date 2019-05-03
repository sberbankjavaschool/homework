package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.List;

public class Converter implements FxConversionService {

    private ExternalQuotesService externalQuotes;

    public Converter(ExternalQuotesService externalQuotesService) {
        externalQuotes = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = externalQuotes.getQuotes(symbol);

        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("No volumes");
        }

        if (amount.signum() <= 0) {
            throw new FxConversionException("Amount less than or equal to 0");
        }

        BigDecimal price;

        switch (operation) {
            case BUY: {
                price = searchQuote(amount, quotes).getOffer();
                break;
            }
            case SELL: {
                price = searchQuote(amount, quotes).getBid();
                break;
            }
            default: {
                throw new FxConversionException("Incorrect operation");
            }
        }

        return price;
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
