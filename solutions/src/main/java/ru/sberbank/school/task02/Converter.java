package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public Converter(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert( ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if(operation == null || symbol == null || amount == null){
            throw new NullPointerException("");
        }

        if(amount.equals(BigDecimal.ZERO))
        {
            throw new IllegalArgumentException();
        }


        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        quotes.sort(Comparator.comparing(Quote::getVolumeSize));

        Quote result = quotes.get(0);

        for(Quote q : quotes){
            if(amount.compareTo(q.getVolumeSize())<=0 ||q.isInfinity()) {
                result = q;
                break;
            }
        }

        return operation == ClientOperation.SELL ? result.getBid() : result.getOffer();
    }


}
