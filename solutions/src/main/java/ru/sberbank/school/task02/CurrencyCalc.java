package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyCalc implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public CurrencyCalc( ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert( ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if(amount.equals(BigDecimal.ZERO))
            return null;

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        Quote currentQuote = quotes.get(0);

        for(Quote quote : quotes){
            if(amount.compareTo(quote.getVolume().getVolume()) <= 0){
                currentQuote = quote;
                break;
            }
        }

        return operation == ClientOperation.BUY ? currentQuote.getOffer() : currentQuote.getBid() ;
    }
}
