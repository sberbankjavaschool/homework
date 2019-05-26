package ru.sberbank.school.task02.util;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
public class ReversedQuote {


    private Symbol symbol;
    private Volume upperBound;
    private Volume lowerBound;
    private ClientOperation direction;
    private BigDecimal price;

    public ReversedQuote(@NonNull Quote quote,
                         @NonNull ClientOperation operation,
                         @NonNull Volume lowerNeighbor) {

        symbol = quote.getSymbol();
        direction = operation;
        if (direction == ClientOperation.BUY) {

            upperBound = Volume.from(quote.getVolumeSize().multiply(quote.getBid()));
            lowerBound = Volume.from(lowerNeighbor.getVolume().multiply(quote.getBid()));
            price = BigDecimal.ONE.divide(quote.getBid(), 10, BigDecimal.ROUND_HALF_UP);
        } else {

            upperBound = Volume.from(quote.getVolumeSize().multiply(quote.getOffer()));
            lowerBound = Volume.from(lowerNeighbor.getVolume().multiply(quote.getOffer()));
            price = BigDecimal.ONE.divide(quote.getOffer(), 10, BigDecimal.ROUND_HALF_UP);
        }
    }
}
