package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExternalQuotesProviderWithOverlapAndHole implements ExternalQuotesService {
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        List<Quote> quotes = new ArrayList<>();
        quotes.add(new Quote(symbol, Volume.from(5875), new BigDecimal(50), new BigDecimal(60)));
        quotes.add(new Quote(symbol, Volume.from(118789), new BigDecimal(53), new BigDecimal(58)));
        quotes.add(new Quote(symbol, Volume.from(4949181), new BigDecimal(54), new BigDecimal(56)));
        quotes.add(new Quote(symbol, Volume.INFINITY, new BigDecimal(49), new BigDecimal(62)));
        return quotes;
    }
}
