package ru.sberbank.school.task02;



import ru.sberbank.school.task02.exception.FxConversionException;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


public class CurrencyCalculator implements FxConversionService {
    private ExternalQuotesService externalQuotesService;

    public CurrencyCalculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        Objects.requireNonNull(operation, "Operation can't be null");
        Objects.requireNonNull(symbol, "Symbol can't be null");
        Objects.requireNonNull(amount, "Amount can't be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount);
        }

        Quote quote = getSuitableQuote(symbol, amount);

        return operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid();
    }

    private Quote getSuitableQuote(Symbol symbol, BigDecimal amount) {
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);

        if (quoteList == null || quoteList.isEmpty()) {
            throw new FxConversionException("There are not quotes for this symbol");
        }

        Quote result = null;

        for (Quote quote : quoteList) {
            result = chooseQuote(amount, result, quote);
        }

        return result;

    }

    protected Quote chooseQuote(BigDecimal amount, Quote quote1, Quote quote2) {

        Objects.requireNonNull(amount, "Amount can't be null");
        Objects.requireNonNull(quote2, "Quote2 can't be null");


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount);
        }

        if (quote1 == null) {
            if (quote2.isInfinity() || (!quote2.isInfinity() && amount.compareTo(quote2.getVolumeSize()) < 0)) {
                return quote2;
            }
        } else {
            if (quote1.isInfinity()) {
                if (amount.compareTo(quote2.getVolumeSize()) < 0) {
                    return  quote2;
                }
            } else {
                if (!quote2.isInfinity() && amount.compareTo(quote2.getVolumeSize()) < 0
                        && quote2.getVolumeSize().compareTo(quote1.getVolumeSize()) < 0) {
                    return quote2;
                }
            }
        }

        return quote1;
    }

}
