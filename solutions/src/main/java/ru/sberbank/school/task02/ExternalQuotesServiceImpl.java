package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.QuoteBuilder;
import ru.sberbank.school.task02.util.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для обработки внешних котировок
 *
 * Created by Gregory Melnikov at 27.04.2019
 */
public class ExternalQuotesServiceImpl implements ExternalQuotesService {

    private static final int[] VOLUMES = {100, 500, 1000, 10_000, 100_000, 1_000_000, -1};
    private static final int[] SPREADS = {10, 8, 6, 5, 4, 6, 8};

    QuoteBuilder quoteBuilder = new QuoteBuilder();

    /**
     * Возвращает список котировок
     *
     * @param symbol - валютная пара, кросс-курсовые пары не допускаются
     * @return список котировок для входящей пары
     */
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        if (symbol.isCross()) {
            throw new WrongSymbolException("Cross symbols are not supported!");
        }

        List<Quote> quotes = new ArrayList<>();

        for (int i = 0; i < VOLUMES.length; i++) {
            Quote quote = quoteBuilder.buildQuote(symbol, VOLUMES[i], SPREADS[i]);
            quotes.add(quote);
        }
        return quotes;
    }
}
