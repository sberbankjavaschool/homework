package ru.sberbank.school.task02.exception;

import lombok.NonNull;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class FxConversionServiceImpl implements FxConversionService {
    private ExternalQuotesService externalQuotesService;

    public FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation, @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {

        if (!(amount.compareTo(BigDecimal.ZERO) > 0)) {
            throw new IllegalArgumentException("Amount меньше или равно нулю");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null) {
            throw new FxConversionException("externalQuotesService пустой");
        }

        Quote bestQuotes = null;
        for (Quote quote : quotes) {
            if (amount.compareTo(quote.getVolumeSize()) < 0 && !quote.isInfinity()) {
                if (bestQuotes == null || bestQuotes.getVolumeSize().compareTo(quote.getVolumeSize()) > 0
                        || bestQuotes.isInfinity()) {
                    bestQuotes = quote;
                }
            } else if (quote.isInfinity() && bestQuotes == null) {
                bestQuotes = quote;
            }
        }

        if (bestQuotes == null) {
            throw new ConverterConfigurationException("Не найден подходящий quotes");
        }

        return operation == ClientOperation.BUY ? bestQuotes.getOffer() : bestQuotes.getBid();
    }
}