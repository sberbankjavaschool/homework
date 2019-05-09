package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExternalQuotesProvider implements ExternalQuotesService {
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        List<Quote> quotes = new ArrayList<>();
        for (int i = 9; i > 0; i--) {
            quotes.add(new Quote(symbol, Volume.from(i * 10), new BigDecimal(i * 100), new BigDecimal(i * 1000)));
        }
//        quotes.add(new Quote(symbol, Volume.INFINITY, new BigDecimal(1000), new BigDecimal(10000)));
        return quotes;
    }
}
