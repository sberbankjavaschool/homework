package ru.sberbank.school.solution02;


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
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);
        Quote targetQuote = quoteList.get(0);
        for (Quote currentQuote : quoteList) {
            if (currentQuote.isInfinity()) {
                targetQuote = currentQuote;
                break;
            }
        }
        for (Quote currentQuote : quoteList) {
            if (currentQuote.getVolumeSize().compareTo(amount) == 0) {
                targetQuote = currentQuote;
                break;
            } else if (currentQuote.getVolumeSize().compareTo(amount) > 0) {
                if (targetQuote.isInfinity()) {
                    targetQuote = currentQuote;
                } else if (targetQuote.getVolumeSize().compareTo(currentQuote.getVolumeSize()) > 0) {
                    targetQuote = currentQuote;
                }
            }
        }
        return ClientOperation.BUY == operation ? targetQuote.getOffer() : targetQuote.getBid();
    }
}
