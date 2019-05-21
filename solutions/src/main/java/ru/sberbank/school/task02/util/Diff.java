package ru.sberbank.school.task02.util;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Diff {

    private BigDecimal delta;
    private boolean isInclusive;

    public Diff(BigDecimal a, BigDecimal b, BigDecimal amount) {

        BigDecimal deltaA = amount.subtract(a).abs();
        BigDecimal deltaB = amount.subtract(b).abs();

        if (deltaA.compareTo(deltaB) < 0) {
            delta = deltaA;
            isInclusive = true;
        } else {
            delta = deltaB;
            isInclusive = false;
        }
    }

    public int compareTo(Diff another) {

        int compare = this.delta.compareTo(another.getDelta());

        if (compare < 0) {
            return -1;
        }

        if (compare > 0) {
            return 1;
        }

        // Если this и another одинаковы по значению, то мы проверяем условие включительно/ не включительно.
        // Включительно значит, что если к amount прибавить/вычесть delta, то значение попадает в интервал.
        // Не включительно значит, что к amount надо прибавить/вычесть amount и еще бесконечно малую величину.
        if (this.isInclusive && !another.isInclusive()) {
            return -1;
        }

        if (!this.isInclusive && another.isInclusive()) {
            return 1;
        }

        return 0;

    }

    public String toString() {
        if (isInclusive) {
            return "[" + delta.toString() + "]";
        } else {
            return "(" + delta.toString() + ")";
        }
    }

}
