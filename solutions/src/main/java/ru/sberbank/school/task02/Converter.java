package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Converter implements FxConversionService {

    private ExternalQuotesService externalQuotes;

    public Converter(ExternalQuotesService externalQuotesService) {
        externalQuotes = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = externalQuotes.getQuotes(symbol);

        if(quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("No volumes");
        }

        Volume volume = Volume.from(amount);

        if (volume.isInfinity()) {
            throw new FxConversionException("Amount less than or equal to 0");
        }

        BigDecimal price;

        switch (operation) {
            case BUY: {
                price = searchQuote(volume, quotes).getOffer();
                break;
            }
            case SELL: {
                price = searchQuote(volume, quotes).getBid();
                break;
            }
            default: {
                throw new FxConversionException("Incorrect operation");
            }
        }

        return price;
    }

    private Quote searchQuote(Volume volume, List<Quote> quotes) {
        Quote searchQuote = null;
        quotes.sort(Comparator.comparing(Quote::getVolumeSize));

        if(volume.getVolume().compareTo(quotes.get(quotes.size() - 1).getVolumeSize()) > 0) {
            throw new FxConversionException("Amount greater than upper bound");
        }

        for (Quote quote : quotes) {
            if (volume.getVolume().compareTo(quote.getVolumeSize()) <= 0) {
                searchQuote = quote;
                break;
            }
        }

        return searchQuote;
    }
}
