package ru.sberbank.school.task02.calculator;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class QuotePrice {
    private BigDecimal amount;
    private BigDecimal price;

    public QuotePrice(BigDecimal amountInCurr, BigDecimal price) {
        this.amount = amountInCurr;
        this.price = price;
    }

    public BigDecimal getPricePerPiece() {
        return BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return  "Price " + price + " amount " + amount + " PricePerPiece: " + getPricePerPiece();
    }
}
