package ru.sberbank.school.task02.calculators;

import lombok.Getter;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
public class CurrencyCalc implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public CurrencyCalc(ExternalQuotesService externalQuotesService) {
        if (externalQuotesService == null) {
            throw new FxConversionException("QuoteService не должен быть равен null");
        }
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation,
                              Symbol symbol, BigDecimal amount) {
        Objects.requireNonNull(operation, "Переданный параметр operation не должен быть null");
        Objects.requireNonNull(symbol, "Переданный параметр symbol не должен быть null");
        Objects.requireNonNull(amount, "Переданный параметр amount не должен быть null");

        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException("Объем должен быть больше 0");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("Список quotes не был сформирован");
        }
        sortQuotes(quotes);
        Quote currentQuote = null;

        for (Quote quote : quotes) {
            if (amount.compareTo(quote.getVolumeSize()) < 0 || quote.isInfinity()) {
                currentQuote = quote;
                break;
            }
        }

        if (currentQuote == null) {
            throw new FxConversionException("Quote не найден");
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
