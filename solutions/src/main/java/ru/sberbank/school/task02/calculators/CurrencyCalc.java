package ru.sberbank.school.task02.calculators;

import lombok.Getter;
import lombok.NonNull;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CurrencyCalc implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public CurrencyCalc(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol, @NonNull BigDecimal amount) {
        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException();
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        sortQuotes(quotes);
        Quote currentQuote = quotes.get(0);

        for (Quote quote : quotes) {
            if (amount.compareTo(quote.getVolumeSize()) < 0 || quote.isInfinity()) {
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
