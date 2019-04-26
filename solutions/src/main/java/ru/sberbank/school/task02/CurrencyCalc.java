package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class CurrencyCalc implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public CurrencyCalc( ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert( ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if(amount == null || operation == null || symbol == null || amount.equals(BigDecimal.ZERO))
            return BigDecimal.ZERO;

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        sortQuotes(quotes);
        Quote currentQuote = quotes.get(0);

        for(Quote quote : quotes){
            if(amount.compareTo(quote.getVolumeSize()) <= 0){
                currentQuote = quote;
                break;
            }
        }

        return operation == ClientOperation.BUY ? currentQuote.getOffer() : currentQuote.getBid() ;
    }

    private void sortQuotes( List<Quote> quotes){
        Collections.sort(quotes, (o1, o2) -> {
            if(o1.getVolumeSize().compareTo(BigDecimal.ZERO) < 0)
                return 1;
            if(o2.getVolumeSize().compareTo(BigDecimal.ZERO) < 0)
                return 0;
            return o1.getVolumeSize().compareTo(o2.getVolumeSize());
        });
    }

}
