package ru.sberbank.school.task02.util;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
public class ReversedQuote {

    private Volume upperBound;
    private Volume lowerBound;
    private ClientOperation direction;
    private BigDecimal price;
    private Quote q;

    public void setLowerBound(Volume lowerBound) {
        this.lowerBound = Volume.from(lowerBound.getVolume().multiply(direction == ClientOperation.SELL
                                                                    ? q.getBid()
                                                                    : q.getOffer()));
    }

    public ReversedQuote(@NonNull Quote quote,
                         @NonNull ClientOperation operation) {

        q = quote;
        direction = operation;

        if (direction == ClientOperation.SELL) {

            upperBound = Volume.from(q.getVolumeSize().multiply(q.getBid()));
            lowerBound = Volume.from(BigDecimal.valueOf(0.01));
            price = BigDecimal.ONE.divide(q.getBid(), 10, BigDecimal.ROUND_HALF_UP);
        } else {

            upperBound = Volume.from(q.getVolumeSize().multiply(q.getOffer()));
            lowerBound = Volume.from(BigDecimal.valueOf(0.01));
            price = BigDecimal.ONE.divide(q.getOffer(), 10, BigDecimal.ROUND_HALF_UP);
        }


    }
}
