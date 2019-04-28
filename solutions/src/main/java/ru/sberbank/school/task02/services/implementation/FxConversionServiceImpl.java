package ru.sberbank.school.task02.services.implementation;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.services.InternalQuotesService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Валютный калькулятор
 *
 * Created by Gregory Melnikov at 27.04.2019
 */
public class FxConversionServiceImpl implements FxConversionService {

    ExternalQuotesService externalQuotesService = new ExternalQuotesServiceImpl();
    InternalQuotesService internalQuotesService = new InternalQuotesServiceImpl(externalQuotesService);

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

//        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        List<Quote> quotes = internalQuotesService.getQuotes(symbol);

        Quote targetQuote = quotes.get(quotes.size()-1);

        for (Quote quote : quotes) {
            if (quote.isInfinity() && amount.compareTo(targetQuote.getVolumeSize()) > 0) {
                targetQuote = quote;
//                continue;
                break;
            }
            if (targetQuote.isInfinity() && amount.compareTo(quote.getVolumeSize()) > 0) {

                System.out.println("FINAL!!!");
                System.out.println(amount.toString() + " " + amount.compareTo(quote.getVolumeSize()) + " " + " " + quote.getVolumeSize());

                break;

            }
            if (amount.compareTo(quote.getVolumeSize()) <= 0) {
                System.out.println("NOT YET FINAL!!!");
                System.out.println(amount.toString() + " " + amount.compareTo(quote.getVolumeSize()) + " " + " " + quote.getVolumeSize());

                targetQuote = quote;
                break;
            }
        }



//        for (Map.Entry<Volume, Quote> entry : quotes.entrySet()) {
//            System.out.println("KEY:" + entry.getKey().toString());
//            System.out.println("VALUE:" + entry.getValue().toString());
//        }


//        return targetQuote.getVolumeSize();
        return ClientOperation.BUY.equals(operation) ? targetQuote.getOffer() : targetQuote.getBid();
//        return BigDecimal.valueOf(1001);
    }
}
