package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

public class MyExternalQuotesService implements ExternalQuotesService {
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
//        if (symbol.isCross()) {
//            throw new WrongSymbolException("Cross symbols are not supported!");
//        }
        return asList(
                buildQuote(symbol, 100_000, 4),
                buildQuote(symbol, 100, 10),
                buildQuote(symbol, -1, 8),
                buildQuote(symbol, 1000, 3),
                buildQuote(symbol, 1000, 7),
                buildQuote(symbol, 1_000_000, 6),
                buildQuote(symbol, 500, 8),
                buildQuote(symbol, 10_000, 5)
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
