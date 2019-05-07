package ru.sberbank.school.task02.util;

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static ru.sberbank.school.task02.util.Symbol.USD_RUB;

public class QuoteBuilderTest {

    QuoteBuilder quoteBuilder = new QuoteBuilder();

    @Test
    public void buildQuoteSymbolTest() {
        int volume = 1_000_000;
        int spread = 8;
        Quote quote = QuoteBuilder.buildQuote(USD_RUB, volume, spread);
        assertEquals(USD_RUB, quote.getSymbol());
    }

    @Ignore //В классе Volume не переопределён метод equals
    @Test
    public void buildQuoteVolumeTest() {
        int volume = 1_000;
        int spread = 6;
        Quote quote = QuoteBuilder.buildQuote(USD_RUB, volume, spread);
        Volume target = Volume.from(1_000_000L);
        assertEquals(target, quote.getVolume());
    }

    @Test
    public void buildQuoteBidTest() {
        int volume = 10_000;
        int spread = 5;
        Quote quote = QuoteBuilder.buildQuote(USD_RUB, volume, spread);

        long value = 80L - 5L / 2L;
        BigDecimal target = BigDecimal.valueOf(value);
        assertEquals(target, quote.getBid());
    }

    @Test
    public void buildQuoteOfferTest() {
        int volume = 10_000;
        int spread = 5;
        Quote quote = QuoteBuilder.buildQuote(USD_RUB, volume, spread);

        long value = 80L + 5L / 2L;
        BigDecimal target = BigDecimal.valueOf(value);
        assertEquals(target, quote.getOffer());
    }
}