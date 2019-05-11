package ru.sberbank.school.task02.services.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.services.InternalQuotesService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

import static ru.sberbank.school.task02.util.ClientOperation.BUY;
import static ru.sberbank.school.task02.validators.AmountPositiveValidator.amountValidate;

/**
 * Валютный калькулятор: возвращает значение цены еденицы базовой валюты для заданного количества котируемой валюты.
 * Для параметров BUY, USD/RUB, 1000, ситуация читается так:
 * Клиент хочет купить 1000 долларов за рубли, сказать сколько будет стоить 1 доллар.
 * Created by Gregory Melnikov at 27.04.2019
 */
@RequiredArgsConstructor
public class FxConversionServiceImpl implements FxConversionService {

    protected final InternalQuotesService internalQuotesService;

    /**
     * Возвращает значение цены единицы базовой валюты для указанного объема.
     *
     * @param operation вид операции
     * @param symbol    Инструмент
     * @param amount    Объем
     * @return Цена для указанного объема
     */
    @Override
    public BigDecimal convert(@NonNull ClientOperation operation, @NonNull Symbol symbol, @NonNull BigDecimal amount) {

        amountValidate(amount);

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
            if (amount.compareTo(quote.getVolumeSize()) < 0) {
                targetQuote = quote;
                break;
            }
        }
        return operation == BUY ? targetQuote.getOffer() : targetQuote.getBid();
    }
}