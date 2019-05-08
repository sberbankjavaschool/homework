package ru.sberbank.school.task02.util;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class FxResponse implements Serializable {
    /**
     * Строковое представление инструмента, например, USD/RUB.
     */
    private final String symbol;
    /**
     * Отформатированное число с 2мя знаками в дробной части.
     */
    private final String price;
    /**
     * Отформатированное число с 2мя знаками в дробной части.
     */
    private final String amount;
    /**
     * Отформатированная дата и время запроса.
     */
    private final String date;
    private final boolean notFound;

    @Override
    public String toString() {
        return symbol + "(" + amount + ")" + ": " + (notFound ? "not found" : amount);
    }
}
