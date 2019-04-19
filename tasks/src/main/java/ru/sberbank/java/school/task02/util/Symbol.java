package ru.sberbank.java.school.task02.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Symbol {
    public static Symbol USD_RUB = new Symbol("USD/RUB", false);
    public static Symbol RUB_USD = new Symbol("RUB/USD", true);
    private final String symbol;
    private boolean isCross;

    private Symbol(String symbol, boolean isCross) {
        this.symbol = symbol;
        this.isCross = isCross;
    }

}
