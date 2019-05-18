package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.util.Quote;

import java.math.BigDecimal;
import java.util.Comparator;

public class CompareQutesExtend implements Comparator<QuotePrice> {

    @Override
    public int compare(QuotePrice quote, QuotePrice t1) {
        BigDecimal quoteVolume1 =  quote.getAmount();
        BigDecimal quoteVolume2 =  t1.getAmount();
        if (quote.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return 1;
        }
        if (t1.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return -1;
        }
        return quoteVolume1.compareTo(quoteVolume2);
    }
}
