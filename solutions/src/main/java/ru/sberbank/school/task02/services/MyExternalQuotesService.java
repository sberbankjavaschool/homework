package ru.sberbank.school.task02.services;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

public class MyExternalQuotesService implements ExternalQuotesService {
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        return asList(
                buildQuote(symbol, 1300, 2),
                buildQuote(symbol, 100, 10),
                buildQuote(symbol, 1000, 40),
                buildQuote(symbol, 1_000_000, 2),
                buildQuote(symbol, 500, 8),
                buildQuote(symbol, 1150, 22),
                buildQuote(symbol, -1, 0)
        );
    }

    private Quote buildQuote(Symbol symbol, int volume, int spread) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(80 - spread / 2))
                .offer(BigDecimal.valueOf(80 + spread / 2))
                .build();
    }
}
