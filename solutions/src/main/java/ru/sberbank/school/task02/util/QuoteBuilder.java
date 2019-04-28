package ru.sberbank.school.task02.util;

import java.math.BigDecimal;

/**
 * Создание котировок
 * Created by Gregory Melnikov at 27.04.2019
 */
public class QuoteBuilder {

    private static long BID_VALUE = 80L;
    private static long OFFER_VALUE = 80L;
    private static long DENOMINATOR = 2L;

    public static Quote buildQuote(Symbol symbol, int volume, int spread) {
        return Quote.builder()
                .symbol(symbol)
                .volume(Volume.from(volume))
                .bid(BigDecimal.valueOf(BID_VALUE - spread / DENOMINATOR))
                .offer(BigDecimal.valueOf(OFFER_VALUE + spread / DENOMINATOR))
                .build();
    }

    public static long getBidValue() {
        return BID_VALUE;
    }

    public static void setBidValue(long bidValue) {
        BID_VALUE = bidValue;
    }

    public static long getOfferValue() {
        return OFFER_VALUE;
    }

    public static void setOfferValue(long offerValue) {
        OFFER_VALUE = offerValue;
    }

    public static long getDenominator() {
        return DENOMINATOR;
    }

    public static void setDenominator(long denominator) {
        QuoteBuilder.DENOMINATOR = denominator;
    }
}
