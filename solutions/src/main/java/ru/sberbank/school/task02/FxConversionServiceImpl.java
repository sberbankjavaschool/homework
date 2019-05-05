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
            throw new NullPointerException("operation is null");
        }

        if (symbol == null) {
            throw new NullPointerException("symbol is null");
        }

        if (amount == null) {
            throw new NullPointerException("amount is null");
        }

        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new ConverterConfigurationException("Wrong operation input: amount is negative or zero");
        }

        List<Quote> quotes = quotesService.getQuotes(symbol);

        if (quotes.isEmpty()) {
            throw new ConverterConfigurationException("List of Quotes for exchange operations is empty");
        }

        Quote correctQuote = quotes.stream()
                .filter(quote -> (amount.compareTo(quote.getVolumeSize()) < 0
                && amount.compareTo(BigDecimal.valueOf(0)) > 0) || quote.isInfinity())
                .min((q1, q2) -> {
                    if (q1.isInfinity()) {
                        return 1;    //reverse it works, but don't know why
                    } else if (q2.isInfinity()) {
                        return -1;   //reverse  it works, but don't know why
                    } else {
                        return q1.getVolumeSize().compareTo(q2.getVolumeSize());
                    }
                })
                .orElse(quotes.get(quotes.size() - 1));

/*       this for recurrent method try
//        for (Quote quote : quotes) {
//            correctQuote = findQuote(quote, correctQuote, amount);
       }
*/

        return operation == ClientOperation.BUY ? correctQuote.getOffer() : correctQuote.getBid();
    }


    /*  One method tries with stream():
        1) realization find min part after filter
        .min(Comparator.comparing(Quote::getVolumeSize))
        2) realization find min part after filter
        .min((q1, q2)-> {
                    if (q1.getVolume().isInfinity()) {
                        return -1;
                    }else if (q2.getVolume().isInfinity()) {
                        return 1;
                    }else return q1.getVolumeSize().compareTo(q2.getVolumeSize());
                })

          Recurrent method try:

          private Quote findQuote(Quote quote, Quote correctQuote, BigDecimal amount) {
        if (correctQuote == null && quote.isInfinity()) {
                correctQuote = quote;
        }else {
            quote.isInfinity();
            if (quote.getVolumeSize().compareTo(amount) > 0){
                if (correctQuote == null || correctQuote.isInfinity()
                || quote.getVolumeSize().compareTo(correctQuote.getVolumeSize()) < 0)
                correctQuote = quote;
            }
        }
        return correctQuote;
    }
         */
}