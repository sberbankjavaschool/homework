package ru.sberbank.school.task02.service;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class FxConversionServiceImpl implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {

        List<Quote> list = externalQuotesService.getQuotes(symbol);

        for (Quote quote : list) {

            if (quote.getVolumeSize().equals(getUpperVolume(symbol, amount))) {
                if (operation.equals(ClientOperation.SELL)) {
                    return quote.getBid();
                } else {
                    return quote.getOffer();
                }
            }
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal getUpperVolume(Symbol symbol, BigDecimal amount){

        BigDecimal max = BigDecimal.ZERO;
        List<Quote> list = externalQuotesService.getQuotes(symbol);

        for (Quote quote : list) {
            max = quote.getVolumeSize().max(max);
        }

            BigDecimal upperVolume = max;
            int traceVolume = 0;

        for (Quote quote : list) {
            if (quote.getVolumeSize().compareTo(amount) == 1) {
                upperVolume = quote.getVolumeSize().min(upperVolume);
            } else {
                traceVolume++;
            }
        }

        if (traceVolume == list.size())
            upperVolume = BigDecimal.valueOf(-1);

        return upperVolume;
    }
}
