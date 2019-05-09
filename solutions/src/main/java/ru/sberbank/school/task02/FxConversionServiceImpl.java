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

<<<<<<< HEAD
    /**
     * Возвращает значение цены единицы базовой валюты для указанного объема.
     *
     * @param operation вид операции
     * @param symbol    Инструмент
     * @param amount    Объем
     * @return Цена для указанного объема
     */
    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);
        if (quoteList.isEmpty()) {
            throw new FxConversionException("Отсутстуют котировки на заданную валютную пару");
        }
=======
    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);
>>>>>>> d546d2fd06db1cff06faa4d90977720619d88ceb
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
