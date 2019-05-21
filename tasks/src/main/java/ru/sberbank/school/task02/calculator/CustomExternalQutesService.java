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
                buildQuote(symbol, 16, 52, 57),
                buildQuote(symbol, 15, 53, 61),
                buildQuote(symbol, 17, 49,56 )
        );
    }

    private Quote buildQuote(Symbol symbol, int volume, int bid, int offer) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(bid))
                .offer(BigDecimal.valueOf(offer))

                .build();
    }
}
