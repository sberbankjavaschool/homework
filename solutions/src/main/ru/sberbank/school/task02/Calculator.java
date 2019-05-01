package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Calculator implements FxConversionService {

    protected ExternalQuotesService externalQuotesService;

    public Calculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {

        if (amount.compareTo(new BigDecimal(0)) < 0 || amount.compareTo(new BigDecimal(0)) == 0) {
            throw new ConverterConfigurationException("Amount less than or equal to 0.");
        }

        // получаем массив котировок
        List<Quote> quotes = getQuotes(symbol);

        // цена единицы базовой валюты
        BigDecimal price = null;

        for (Quote q : quotes) {
            // если volume == -1 или amount <= volume, то мы нашли нашу котировку
            if (q.isInfinity() || amount.compareTo(q.getVolumeSize()) < 0 || amount.compareTo(q.getVolumeSize()) == 0) {
                price = operation.compareTo(ClientOperation.BUY) == 0 ? q.getOffer() : q.getBid();
                break;
            }
        }

        return price;
    }

    private List<Quote> getQuotes(Symbol symbol) {
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes == null || quotes.isEmpty()) {
            throw new ConverterConfigurationException("No quotes.");
        }

        // quotes.sort(Comparator.comparing(quote -> quote.getVolumeSize()));
        // Comparator<Quote> comparator = Comparator.comparing(quote -> quote.getVolumeSize());
        // quotes.sort(comparator.reversed());

        // сортируем массив котировок по возрастанию с учетом -1=infinity
        Comparator<Quote> comparator = new Comparator<Quote>() {
            public int compare(Quote q1, Quote q2) {
                if (q1.isInfinity()) return 1;
                if (q2.isInfinity()) return -1;
                return q1.getVolumeSize().compareTo(q2.getVolumeSize());
            }
        };
        return quotes;
    }

}