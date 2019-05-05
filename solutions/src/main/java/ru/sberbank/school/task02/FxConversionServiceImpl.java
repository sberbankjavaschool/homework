package ru.sberbank.school.task02;


import ru.sberbank.school.task02.exception.FxConversionException;
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
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);
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
