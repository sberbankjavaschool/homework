package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class Calculator implements FxConversionService {
    private ExternalQuotesService provider;

    Calculator(ExternalQuotesService externalQuotesService) {
        this.provider = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = provider.getQuotes(symbol);
        quotes.sort((right, left)-> {
            if (left.isInfinity()) return 1;
            if (right.isInfinity()) return -1;
            return left.getVolumeSize().compareTo(right.getVolumeSize());
        });
        BigDecimal currentOffer = BigDecimal.ZERO;
        for (Quote quote: quotes) {
            if (quote.isInfinity()) {
                currentOffer = quote.getOffer();
            } else if (amount.compareTo(quote.getVolumeSize()) < 0) {
                currentOffer = quote.getOffer();
            } else {
                break;
            }
        }
        return currentOffer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calculator that = (Calculator) o;
        return provider != null ? provider.equals(that.provider) : that.provider == null;
    }

    @Override
    public int hashCode() {
        return provider != null ? provider.hashCode() : 0;
    }
}
