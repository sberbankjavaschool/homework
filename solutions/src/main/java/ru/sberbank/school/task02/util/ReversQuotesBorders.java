package ru.sberbank.school.task02.util;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class ReversQuotesBorders {
    private Quote quote;
    private BigDecimal lowerBorder;
    private BigDecimal upperBorder;

    public ReversQuotesBorders(Quote prefer, Quote current, ClientOperation operation) {
        this.quote = current;
        this.lowerBorder = calculateLowerBorder(prefer, operation);
        this.upperBorder = calculateUpperBorder(current, operation);

    }

    private BigDecimal calculateLowerBorder(Quote prefer, ClientOperation operation) {
        if (prefer == null) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal price = operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid();
            return price.multiply(prefer.getVolumeSize());
        }
    }

    private BigDecimal calculateUpperBorder(Quote current, ClientOperation operation) {
        if (current.isInfinity()) {
            return null;
        } else {
            BigDecimal price = operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid();
            return price.multiply(current.getVolumeSize());
        }
    }
}
