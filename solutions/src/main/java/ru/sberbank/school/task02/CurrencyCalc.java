package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyCalc implements FxConversionService {

    protected ExternalQuotesService externalQuotesService;

    public CurrencyCalc(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null) {
            throw new NullPointerException("operation missing");
        }
        if (amount == null) {
            throw new NullPointerException("amount missing");
        }
        if (symbol == null) {
            throw new NullPointerException("symbol missing");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount should be positive");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("quotes unavailable");
        }

        Quote quote = pickQuote(quotes, amount);

        return operation == ClientOperation.SELL ? quote.getBid() : quote.getOffer();
    }

    private Quote pickQuote(List<Quote> quoteList, BigDecimal amount) {
        Quote pickedQuote = null;

        for (Quote tempQuote : quoteList) {
            if (pickedQuote == null && tempQuote.isInfinity()) {
                pickedQuote = tempQuote;
            } else if (tempQuote.getVolumeSize().compareTo(amount) > 0) {
                if (pickedQuote == null
                        || pickedQuote.isInfinity()
                        || tempQuote.getVolumeSize().compareTo(pickedQuote.getVolumeSize()) < 0) {
                    pickedQuote = tempQuote;
                }
            }
        }

        if (pickedQuote == null) {
            throw new FxConversionException("no matching quote");
        }

        return pickedQuote;
    }
}