package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

public class CustomExternalQutesService implements ExternalQuotesService {

    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        if (symbol.isCross()) {
            throw new WrongSymbolException("Cross symbols are not supported!");
        }
        return asList(
                buildQuote(symbol, BigDecimal.valueOf(4739.6387275476  ), 50, 60),
                buildQuote(symbol, BigDecimal.valueOf(164154.6297156020  ), 53, 58),
                buildQuote(symbol, BigDecimal.valueOf(7478646.7822561432 ), 54, 56),
                buildQuote(symbol, BigDecimal.valueOf(-1), 49, 62)
           );
    }

    private Quote buildQuote(Symbol symbol, BigDecimal volume, int spread) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(55 - spread / 2))
                .offer(BigDecimal.valueOf(55 + spread / 2))
                .build();
    }
    private Quote buildQuote(Symbol symbol, BigDecimal volume, int bid, int offer) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(bid))
                .offer(BigDecimal.valueOf(offer))
                .build();
    }
}
