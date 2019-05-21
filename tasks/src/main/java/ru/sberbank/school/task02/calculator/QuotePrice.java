package ru.sberbank.school.task02.calculator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class QuotePrice {
    private BigDecimal amount;
    private BigDecimal price;
    @Setter
    private BigDecimal lowerLimit;

    public QuotePrice(BigDecimal amountInCurr, BigDecimal price) {
        this.amount = amountInCurr;
        this.price = price;
    }

    public BigDecimal getUpperLimit() {
        return amount;
    }

    public BigDecimal getPricePerPiece() {
        return BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return  "Price " + price + " amount " + amount + " PricePerPiece: " + getPricePerPiece();
    }

    private boolean moreThenLowerLimit (BigDecimal amount) {
        return amount.compareTo(lowerLimit) > 0;
    }

    private boolean lessThenUpperLimit (BigDecimal amount) {
        return amount.compareTo(getUpperLimit()) < 0
                || getUpperLimit().compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean checkQuoteInMyScope(BigDecimal amount) {
        BigDecimal amountInCurr = amount.divide(price,10, RoundingMode.HALF_UP);
        return moreThenLowerLimit(amountInCurr) && lessThenUpperLimit(amountInCurr);
    }
}
