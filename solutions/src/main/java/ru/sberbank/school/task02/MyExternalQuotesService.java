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
        if (symbol.isCross()) {
            throw new WrongSymbolException("Cross symbols are not supported!");
        }
        return asList(
                buildQuote(symbol, 1000, 12),
                buildQuote(symbol, 500, 10),
                buildQuote(symbol, 1200, 8),
                buildQuote(symbol, 100, 4),
                buildQuote(symbol, -1, 6),
                buildQuote(symbol, 800, 2)
        );
    }

    private Quote buildQuote(Symbol symbol, int volume, int spread) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(48 + spread / 2))
                .offer(BigDecimal.valueOf(48 + spread / 2))
                .build();
    }
}
