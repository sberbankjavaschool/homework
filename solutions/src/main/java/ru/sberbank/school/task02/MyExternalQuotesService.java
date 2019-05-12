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
                buildQuote(symbol, 80, 10),
                buildQuote(symbol, 102_423, 8),
                buildQuote(symbol, 1, 6),
                buildQuote(symbol, -1, 5),
                buildQuote(symbol, 100_00, 4),
                buildQuote(symbol, 1_000_000, 6),
                buildQuote(symbol, 4902, 8)
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
