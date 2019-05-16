package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {
    private ExternalQuotesService externalQuotesService;

    public Converter(@NonNull ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert(@NonNull ClientOperation operation, @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Введите корректное значение объема!");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("Список валют пуст!");
        }

        Quote result = null;

        for (Quote quote : quotes) {
            if (amount.compareTo(quote.getVolumeSize()) < 0 || quote.isInfinity()) {
                if (result == null || quoteComparison(quote,result) < 0) {
                    result = quote;
                }
            }
        }

        if (result == null) {
            throw new FxConversionException("Подходящей котировки нет!");
        }

        boolean isSell = operation == ClientOperation.SELL;
        return isSell ? result.getBid() : result.getOffer();
    }

    private int quoteComparison(Quote lq, Quote rq) {
        if (lq.isInfinity()) {
            return 1;
        }

        if (rq.isInfinity()) {
            return -1;
        }
        return lq.getVolumeSize().compareTo(rq.getVolumeSize());
    }
}


