package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.util.Quote;

import java.math.BigDecimal;
import java.util.Comparator;

public class CompareQutes implements Comparator<Quote> {

    @Override
    public int compare(Quote quote, Quote t1) {
        BigDecimal quoteVolume1 =  quote.getVolume().getVolume();
        BigDecimal quoteVolume2 =  t1.getVolume().getVolume();
        if (quote.getVolume().isInfinity()) {
            return 1;
        }
        if (t1.getVolume().isInfinity()) {
            return -1;
        }
        return quoteVolume1.compareTo(quoteVolume2);
    }


}
