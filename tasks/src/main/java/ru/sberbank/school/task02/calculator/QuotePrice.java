package ru.sberbank.school.task02.calculator;

import lombok.Getter;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;

import java.math.BigDecimal;

@Getter
public class QuotePrice {
    private BigDecimal amountInCurr;
    private BigDecimal price;

    public QuotePrice(BigDecimal amountInCurr, BigDecimal price) {
        this.amountInCurr = amountInCurr;
        this.price = price;
    }

    @Override
    public String toString() {
        return  "Price " + price + " amount of all " + amountInCurr;
    }
}
