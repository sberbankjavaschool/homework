package ru.sberbank.school.task02;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ExternalQuotesProviderTest {

    @Test
    public void getQuotesReturnsQuotes() {
        ExternalQuotesService quotesService = new ExternalQuotesProvider();
        Assert.assertEquals(quotesService.getQuotes(Symbol.USD_RUB).size(), 10);
    }
}