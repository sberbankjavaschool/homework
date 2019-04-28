package ru.sberbank.school.task02.services.implementation;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.services.InternalQuotesService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

/**
 * Валютный калькулятор
 * Created by Gregory Melnikov at 27.04.2019
 */
@RequiredArgsConstructor
public class FxConversionServiceImpl implements FxConversionService {

    private final ExternalQuotesService externalQuotesService; //= new ExternalQuotesServiceImpl();

    private final InternalQuotesService internalQuotesService = new InternalQuotesServiceImpl(externalQuotesService);

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

        List<Quote> quotes = internalQuotesService.getQuotes(symbol);

        Quote targetQuote = quotes.get(quotes.size() - 1);

        for (Quote quote : quotes) {
            if (quote.isInfinity() && amount.compareTo(targetQuote.getVolumeSize()) > 0) {
                targetQuote = quote;
                break;
            }
            if (targetQuote.isInfinity() && amount.compareTo(quote.getVolumeSize()) > 0) {
                break;
            }
            if (amount.compareTo(quote.getVolumeSize()) <= 0) {
                targetQuote = quote;
                break;
            }
        }
        return ClientOperation.BUY.equals(operation) ? targetQuote.getOffer() : targetQuote.getBid();
    }
}
