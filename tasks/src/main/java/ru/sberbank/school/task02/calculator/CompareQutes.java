package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.util.Quote;

import java.math.BigDecimal;
import java.util.Comparator;

public class CompareQutes implements Comparator<Quote> {

    @Override
    public int compare(Quote quote, Quote t1) {
        BigDecimal quoteVolume1 =  quote.getVolume().getVolume();
        BigDecimal quoteVolume2 =  t1.getVolume().getVolume();
        return quoteVolume1.compareTo(quoteVolume2) > 0 ? 1:quoteVolume1.compareTo(quoteVolume2) == 0 ? 0:-1;
    }
}
