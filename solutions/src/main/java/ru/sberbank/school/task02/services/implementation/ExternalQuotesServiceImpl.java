package ru.sberbank.school.task02.services.implementation;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.school.task02.util.QuoteBuilder.buildQuote;

/**
 * Сервис для обработки внешних котировок
 * Created by Gregory Melnikov at 27.04.2019
 */
public class ExternalQuotesServiceImpl implements ExternalQuotesService {

//    private static final int[] VOLUMES = {100, 300, 100_000, 1000, -1, 500, 1000, -1, 10_000, 100_000, 38000, -1, 1};
//    private static final int[] SPREADS = {10, 9, 15, 10, 9, 8, 6, 4, 5, 6, 8, 40, 1};

    private static final int[] VOLUMES = {100, 30000, 100_001, 100_002, -1, 1};
    private static final int[] SPREADS = {10,   8,      6,       4,     40, 11};

    /**
     * 28.04.2019
     * Возвращает список котировок
     *
     * @param symbol валютная пара, кросс-курсовые пары не допускаются
     * @return список котировок для входящей пары
     */
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        if (symbol.isCross()) {
            throw new WrongSymbolException("Cross symbols are not supported!");
        }

        List<Quote> quotes = new ArrayList<>();

        for (int i = 0; i < VOLUMES.length; i++) {
            Quote quote = buildQuote(symbol, VOLUMES[i], SPREADS[i]);
            quotes.add(quote);
        }
        return quotes;
    }
}
