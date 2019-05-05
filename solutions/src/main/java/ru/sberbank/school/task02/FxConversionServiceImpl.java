package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mart
 * 04.05.2019
 **/
public class FxConversionServiceImpl implements FxConversionService {
    private ExternalQuotesService quotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        this.quotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null) {
            throw new ConverterConfigurationException("Wrong operation input: operation is null");
        }

        if (symbol == null) {
            throw new ConverterConfigurationException("Wrong operation input: symbol is null");
        }

        if (amount == null) {
            throw new ConverterConfigurationException("Wrong operation input: amount is null");
        }

        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new ConverterConfigurationException("Wrong operation input: amount is negative or zero");
        }

        List<Quote> quotes = quotesService.getQuotes(symbol);

        if (quotes.isEmpty()) {
            throw new ConverterConfigurationException("List of Quotes for exchange operations is empty");
        }

        Quote correctQuote;

        /*
        1) realization find min part
        .min(Comparator.comparing(Quote::getVolumeSize))
        2) realization find min part
        .min((q1, q2)-> {
                    if (q1.getVolume().isInfinity()) {
                        return -1;
                    }else if (q2.getVolume().isInfinity()) {
                        return 1;
                    }else return q1.getVolumeSize().compareTo(q2.getVolumeSize());
                })
         */
        correctQuote =  quotes.stream()
                .filter(quote -> quote.getVolumeSize().compareTo(amount) >= 0)
                .min(Comparator.comparing(Quote::getVolumeSize))
                .orElse(quotes.get(0));
        return operation == ClientOperation.BUY ? correctQuote.getOffer() : correctQuote.getBid();
    }
}