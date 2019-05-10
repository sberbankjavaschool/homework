package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

import static ru.sberbank.school.task02.util.ClientOperation.BUY;

public class SimpleFxConversionService implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public SimpleFxConversionService(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null) {
            throw new IllegalArgumentException("operation is not defined");
        }

        if (symbol == null) {
            throw new IllegalArgumentException("instrument is not defined");
        }

        if (amount == null || (amount.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("amount is not defined or not positive");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        Quote quote = null;
        for (Quote quoteCandidate : quotes) {
            if (quoteCandidate == null) {
                continue;
            }

            if (isQuoteVolumeBigger(quoteCandidate, amount)) {
                if (quote == null || quote.isInfinity() || isQuoteVolumeBigger(quote, quoteCandidate.getVolumeSize())) {
                    quote = quoteCandidate;
                }
            } else if (quoteCandidate.isInfinity()) {
                if (quote == null) {
                    quote = quoteCandidate;
                }
            }
        }

        if (quote == null) {
            throw new IllegalStateException("No acceptable quotes were found for given");
        }

        return operation == BUY ? quote.getOffer() : quote.getBid();
    }

    private boolean isQuoteVolumeBigger(Quote quote, BigDecimal amount) {
        return quote.getVolumeSize().compareTo(amount) > 0;
    }
}
