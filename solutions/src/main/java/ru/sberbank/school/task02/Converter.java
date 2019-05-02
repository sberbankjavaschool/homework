package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Converter implements FxConversionService {

    private ExternalQuotesService externalQuotes;

    public Converter(ExternalQuotesService externalQuotesService){
        externalQuotes = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = externalQuotes.getQuotes(symbol);
        Volume volume = Volume.from(amount);

        if(volume.isInfinity()) throw new FxConversionException("Сумма меньше или равна 0");

        BigDecimal price = new BigDecimal(-1);

        switch(operation){
            case BUY: {
                price = getSearchQuote(volume, quotes).getOffer();
                break;
            }
            case SELL: {
                price = getSearchQuote(volume, quotes).getBid();
                break;
            }
        }

        return price;
    }

    private Quote getSearchQuote(Volume volume,  List<Quote> quotes){
        Quote searchQuote = null;
        quotes.sort(Comparator.comparing(Quote::getVolumeSize));

        for (Quote quote : quotes){
            if(volume.getVolume().compareTo(quote.getVolumeSize()) <= 0){
                searchQuote = quote;
                break;
            }
        }

        return searchQuote;
    }
}
