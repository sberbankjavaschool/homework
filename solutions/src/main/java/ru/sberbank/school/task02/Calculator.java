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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        List<Quote> quotes = provider.getQuotes(symbol);
        if (quotes.size() == 0) {
            return null;
        }
        quotes.sort((right, left) -> {
            if (left.isInfinity()) {
                return 1;
            }
            if (right.isInfinity()) {
                return -1;
            }
            return left.getVolumeSize().compareTo(right.getVolumeSize());
        });

        Quote current = quotes.get(0);
        for (Quote quote: quotes) {
            if (quote.isInfinity() || amount.compareTo(quote.getVolumeSize()) < 0) {
                current = quote;
            } else {
                break;
            }
        }

//        return operation == ClientOperation.BUY ? current.getOffer() : current.getBid();
        return current.getOffer();
    }
}
