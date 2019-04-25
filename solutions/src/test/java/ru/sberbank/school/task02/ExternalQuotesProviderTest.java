package ru.sberbank.school.task02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task02.util.Symbol;

class ExternalQuotesProviderTest {

    @Test
    void getQuotesReturnsQuotes() {
        ExternalQuotesService quotesService = new ExternalQuotesProvider();
        Assertions.assertEquals(quotesService.getQuotes(Symbol.USD_RUB).size(), 10);
    }
}