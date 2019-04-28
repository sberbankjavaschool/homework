package ru.sberbank.school.task02;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.services.implementation.ExternalQuotesServiceImpl;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;

public class ExternalQuotesServiceImplTest {

    ExternalQuotesServiceImpl externalQuotesService = new ExternalQuotesServiceImpl();

    @Test
    public void getQuotesIsNotEmpty() {
        List<Quote> quotes = new ArrayList<>();
        quotes = externalQuotesService.getQuotes(Symbol.USD_RUB);

        for (Quote quote : quotes) {
            System.out.println(quote.toString());
            System.out.println("AND VOLUME:" + quote.getVolume().toString());
        }
        assertNotEquals(0, quotes.size());
    }

    @Test(expected = WrongSymbolException.class)
    public void getQuotesNegativeTest() {
        externalQuotesService.getQuotes(Symbol.RUB_USD);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getQuotesNegativeTestWithMessage() throws WrongSymbolException {
        thrown.expect(WrongSymbolException.class);
        thrown.expectMessage(equalTo("Cross symbols are not supported!"));
        externalQuotesService.getQuotes(Symbol.RUB_USD);
        thrown = ExpectedException.none();
    }
}