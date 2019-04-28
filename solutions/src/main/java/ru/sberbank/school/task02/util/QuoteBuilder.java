package ru.sberbank.school.task02.util;

import java.math.BigDecimal;

/**
 * Создание котировок
 *
 * Created by Gregory Melnikov at 27.04.2019
 */
public class QuoteBuilder {

    private static final long BID_CONSTANT = 80L;
    private static final long OFFER_CONSTANT = 80L;
    private static final long DENOMINATOR = 2L;

    public Quote buildQuote(Symbol symbol, int volume, int spread) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(BID_CONSTANT - spread / DENOMINATOR))
                .offer(BigDecimal.valueOf(OFFER_CONSTANT + spread / DENOMINATOR))
                .build();
    }
}
