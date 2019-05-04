package ru.sberbank.school.task02;


import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class FxConversionServiceC implements FxConversionService {
    /**
     * Возвращает значение цены единицы базовой валюты для указанного объема.
     *
     * @param operation вид операции
     * @param symbol    Инструмент
     * @param amount    Объем
     * @return Цена для указанного объема
     */
    private ExternalQuotesService exQuotes;

    public FxConversionServiceC(ExternalQuotesService externalQuotesService) {
        this.exQuotes = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = exQuotes.getQuotes(symbol);
        Quote exRate = null;
        if (quotes.isEmpty()) {
            return null;
        }
        for (Quote q : quotes) {
            if (q.isInfinity() && exRate == null) {
                exRate = q;
            } else if ((amount.compareTo(q.getVolumeSize()) < 0) &&
                    ((exRate == null) || (exRate.isInfinity()) || (amount.compareTo(exRate.getVolumeSize())>0))) {
                exRate = q;
            }
        }
        BigDecimal rate = (operation == ClientOperation.BUY ? exRate.getOffer() : exRate.getBid());
        return rate;
    }

}

