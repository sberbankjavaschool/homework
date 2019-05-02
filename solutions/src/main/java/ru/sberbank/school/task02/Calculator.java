package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Calculator implements FxConversionService {

    protected ExternalQuotesService externalQuotesService;

    public Calculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {

        if (operation == null || symbol == null || amount == null) {
            throw new ConverterConfigurationException("Parameters should be null!");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConverterConfigurationException("Amount less than or equal to 0!");
        }

        // получаем отсортированный массив котировок
        List<Quote> quotes = getSortedQuotes(symbol);

        // цена единицы базовой валюты для заданного объема amount
        BigDecimal price = null;

        for (Quote q : quotes) {
            // если volume == -1 или amount <= volume, то мы нашли нашу котировку
            if (q.isInfinity() || amount.compareTo(q.getVolumeSize()) < 0) {
                price = operation.compareTo(ClientOperation.BUY) == 0 ? q.getOffer() : q.getBid();
                break;
            }
        }

        return price;
    }

    private List<Quote> getSortedQuotes(Symbol symbol) {
        // получаем массив котировок
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes == null || quotes.isEmpty()) {
            throw new ConverterConfigurationException("No quotes!");
        }

        // сортируем массив котировок по возрастанию с учетом -1=infinity
        Comparator<Quote> comparator = new Comparator<Quote>() {
            public int compare(Quote q1, Quote q2) {
                if (q1.isInfinity()) {
                    return 1;
                }

                if (q2.isInfinity()) {
                    return -1;
                }

                return q1.getVolumeSize().compareTo(q2.getVolumeSize());
            }
        };
        quotes.sort(comparator);
        return quotes;
    }

}