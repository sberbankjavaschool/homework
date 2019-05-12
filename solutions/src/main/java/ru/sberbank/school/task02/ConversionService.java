package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;


public class ConversionService implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public ConversionService(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        try {
            if(operation == null || symbol == null || amount.compareTo(BigDecimal.ZERO) < 0 || amount == null)
                throw new IllegalArgumentException();
        }
        catch (IllegalArgumentException ex){
            //System.out.println("incorrect data entered");
            return null;
        }
        if(amount.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        Quote quote = getQuote(symbol, amount);

        return getPrice(operation, quote);
    }

    private Quote getQuote(Symbol symbol,BigDecimal amount) {
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        return getNearQuoteOfVolume(quotes, amount);
    }

    private Quote getNearQuoteOfVolume(List<Quote> quotes, BigDecimal amount) {
        Quote nearQuote = null;
        Quote infQuote = null;
        int i;
        for (i = 0; i < quotes.size(); i++) {
            if(quotes.get(i).isInfinity()) {
                infQuote = quotes.get(i);
                continue;
            }
            if (amount.compareTo(quotes.get(i).getVolumeSize()) > 0) {
                continue;
            } else {
                nearQuote = quotes.get(i);
                break;
            }
        }
        for (int j = i; j < quotes.size(); j++) {
            if(quotes.get(j).isInfinity()) {
                infQuote = quotes.get(j);
                continue;
            }
            if (amount.compareTo(quotes.get(j).getVolumeSize()) >= 0) {
                continue;
            } else {
                if(nearQuote.getVolume().getVolume().compareTo(quotes.get(j).getVolumeSize()) > 0){
                    nearQuote = quotes.get(j);
                }
            }
        }
        if(nearQuote == null)
            return infQuote;
        else
            return nearQuote;
    }

    private BigDecimal getPrice(ClientOperation operation, Quote quote){
        switch (operation){
            case BUY:
                return quote.getOffer();
            case SELL:
                return quote.getBid();
        }
        return null;
    }
}
