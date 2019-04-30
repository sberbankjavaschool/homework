package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyCalculator implements FxConversionService {
    private ExternalQuotesService externalQuotesService;

    public CurrencyCalculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null || symbol == null || amount == null) {
            throw new NullPointerException("Arguments can't be null:\n" + "Operation: " + operation + "\n"
                    + "Symbol: " + symbol + "\n" + "Amount: " + amount);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount);
        }

        Quote quote = getSuitableQuote(symbol, amount);

        return  operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid();
    }

    private Quote getSuitableQuote(Symbol symbol, BigDecimal amount) {
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);

        if (quoteList.isEmpty()) {
            throw  new IllegalArgumentException("There are not quotes for this symbol");
        }

        Quote result = null;
        Quote infinityResult = null;
        BigDecimal diff = null;

        for (Quote quote : quoteList) {
            if (amount.compareTo(quote.getVolumeSize()) < 0 && !quote.isInfinity()) {
                BigDecimal localDiff = quote.getVolumeSize().subtract(amount);

                if (diff == null) {
                    result = quote;
                    diff = localDiff;
                } else if (diff.compareTo(localDiff) > 0) {
                    result = quote;
                    diff = localDiff;
                }
            } else if (quote.isInfinity()) {
                infinityResult = quote;
            }
        }

        return result != null ? result : infinityResult;

    }

}
