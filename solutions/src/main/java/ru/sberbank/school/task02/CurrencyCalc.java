package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CurrencyCalc implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public CurrencyCalc(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (amount == null || operation == null || symbol == null || amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException();
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        sortQuotes(quotes);
        Quote currentQuote = quotes.get(0);
        amount = amount.setScale(0, RoundingMode.HALF_UP);

        for (Quote quote : quotes) {
            if (amount.compareTo(quote.getVolumeSize()) <= 0 || quote.isInfinity()) {
                currentQuote = quote;
                break;
            }
        }

        return operation == ClientOperation.BUY ? currentQuote.getOffer() : currentQuote.getBid();
    }

    private void sortQuotes(List<Quote> quotes) {
        quotes.sort( (o1, o2) -> {
            if (o1.isInfinity()) {
                return 1;
            }
            if (o2.isInfinity()) {
                return -1;
            }
            return o1.getVolumeSize().compareTo(o2.getVolumeSize());
        });
    }

}
