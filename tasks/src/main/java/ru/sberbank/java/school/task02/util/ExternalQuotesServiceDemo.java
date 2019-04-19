package ru.sberbank.java.school.task02.util;

import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.java.school.task02.ExternalQuotesService;
import ru.sberbank.java.school.task02.exception.WrongSymbolException;

import static java.util.Arrays.asList;

public class ExternalQuotesServiceDemo implements ExternalQuotesService {

    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        if (symbol.isCross()) {
            throw new WrongSymbolException("Cross symbols are not supported!");
        }
        return asList(
                buildQuote(symbol, 100, 10),
                buildQuote(symbol, 500, 8),
                buildQuote(symbol, 1000, 6),
                buildQuote(symbol, 10_000, 5),
                buildQuote(symbol, 100_000, 4),
                buildQuote(symbol, 1_000_000, 6),
                buildQuote(symbol, -1, 8)
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
