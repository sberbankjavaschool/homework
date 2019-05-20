package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import java.math.BigDecimal;
import java.util.List;

public class FxConversionServiceImpl implements FxConversionService {
    protected ExternalQuotesService externalQuotesService;

    public FxConversionServiceImpl(@NonNull ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Объем не может быть отрицательным или равным нулю");
        }
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);
        if (quoteList == null || quoteList.isEmpty()) {
            throw new FxConversionException("Отсутстуют котировки на заданную валютную пару");
        }
        Quote targetQuote = null;
        for (Quote currentQuote : quoteList) {
            if (targetQuote == null && currentQuote.isInfinity()) {
                targetQuote = currentQuote;
            } else if (currentQuote.getVolumeSize().compareTo(amount) > 0) {
                if (targetQuote == null || targetQuote.isInfinity()) {
                    targetQuote = currentQuote;
                } else if (targetQuote.getVolumeSize().compareTo(currentQuote.getVolumeSize()) > 0) {
                    targetQuote = currentQuote;
                }
            }
        }
        if (targetQuote == null) {
            throw new FxConversionException("Нет подходящего диапазона котировок");
        }
        return ClientOperation.BUY == operation ? targetQuote.getOffer() : targetQuote.getBid();
    }
}
