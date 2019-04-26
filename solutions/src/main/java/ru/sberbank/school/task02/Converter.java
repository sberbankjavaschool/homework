package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Converter implements FxConversionService {

    private ExternalQuotesService quotesService;

    public Converter(ExternalQuotesService quotesService){
        this.quotesService = quotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {

        List<Quote> quotes = quotesService.getQuotes(symbol);
        if(amount.compareTo(BigDecimal.ZERO) <= 0 || quotes.size() <= 0){
            return null;
        }

        Quote matchedQuote = null;

        quotes.sort(new Comparator<Quote>() {
            @Override
            public int compare(Quote o1, Quote o2) {
                if(o1.isInfinity()){
                    return 1;
                }
                if(o2.isInfinity()){
                    return 0;
                }
                return o1.getVolumeSize().compareTo(o2.getVolumeSize());
            }
        });

        matchedQuote = getMatchedQuote(quotes,amount);

        if(matchedQuote != null){
            if(operation == ClientOperation.BUY){
                return matchedQuote.getOffer();
            }else{
                return matchedQuote.getBid();
            }
        }

        return null;
    }

    private Quote getMatchedQuote(List<Quote> quotes, BigDecimal amount){
        Quote res = null;
        for (Quote q : quotes) {
            if(amount.compareTo(q.getVolumeSize()) <= 0){
                res = q;
                break;
            }
        }
        if(res == null){
            res = getInfQuote(quotes);
        }
        return res;
    }

    private Quote getInfQuote(List<Quote> quotes){
        for (Quote q : quotes) {
            if(q.isInfinity()){
                return q;
            }
        }
        return null;
    }
}
