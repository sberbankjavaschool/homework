package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;
import ru.sberbank.school.task02.exception.*;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyCalc implements FxConversionService {

    private ExternalQuotesService quotes;

    public CurrencyCalc(ExternalQuotesService externalQuotes) {
        this.externalQuotes = externalQuotes;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if(operation == null){
            throw new NullPointerException("operation missing");
        }
        if(amount == null){
            throw new NullPointerException("amount missing");
        }
        if(symbol == null ){
            throw new NullPointerException("symbol missing");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount should be positive");
        }

        List<Quote> quotesList = quotes.getQuotes(symbol);
        if(quotesList.isEmpty()) {
            throw new FxConversionException("quotes unavailable")
        }

        Quote quote = pickQuote(quotesList, amount);

        return operation == ClientOperation.SELL ? quote.getBid() : quote.getOffer();
    }

    private Quote pickQuote(List<Quote> quoteList, BigDecimal amount) {
        Quote pickedQuote = null;

        for(Quote possibleQuote : quoteList){
            if(compareVolumes(getVolumeSize, amount) > 0) {
                if(pickedQuote == null ||
                    compareVolumes(pickedQuote.getVolumeSize(), possibleQuote.getVolumeSize())  < 0 ) {
                    pickedQuote = possibleQuote;
                }
            }
        }

        return pickedQuote == null ? quoteList.get(0) : pickedQuote;
    }

    private int compareVolumes(BigDecimal v1, BigDecimal v2) {
        if(v1.compareTo(BigDecimal.ZERO) < 0) {
            return 1;
        }
        if(v2.compareTo(BigDecimal.ZERO) < 0) {
            return -1;
        }

        return v1.compareTo(v2));
    }
}