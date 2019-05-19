package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import static java.util.Arrays.asList;

public class ExternalQuotesServiceImpl implements ExternalQuotesService {

    public List<Quote> getQuotes(@NonNull Symbol symbol) {
        if (symbol.isCross()) {
            throw new WrongSymbolException("Cross symbols detected");
        }

        List<Quote> quotes = asList(
                buildQuote(symbol, 100, 10),
                buildQuote(symbol, 500, 8),
                buildQuote(symbol, 100, 12),
                buildQuote(symbol, 1000, 6),
                buildQuote(symbol, 10_000, 5),
                buildQuote(symbol, 100_000, 4),
                buildQuote(symbol, 1_000, 4),
                buildQuote(symbol, 100_000, 3),
                buildQuote(symbol, 1_000_000, 6),
                buildQuote(symbol, -1, 8)
        );
        return quotes;
    }

    private Quote buildQuote(@NonNull Symbol symbol, int volume, int spread) {
        return new Quote(symbol, Volume.from(volume), (BigDecimal.valueOf(80 - spread / 2)),
                (BigDecimal.valueOf(80 + spread / 2)));
    }
}
