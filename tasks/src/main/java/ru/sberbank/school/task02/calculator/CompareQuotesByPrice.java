package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.Comparator;

public class CompareQuotesByPrice implements Comparator<Quote> {
    private Beneficiary beneficiary;
    private ClientOperation operation;

    public CompareQuotesByPrice(ClientOperation operation, Beneficiary beneficiary) {

        this.beneficiary = beneficiary;
        this.operation = operation;
    }

    @Override
    public int compare(Quote quote1, Quote quote2) {
        return compareEqualQuotes(quote1, quote2);
    }

    private int compareEqualQuotes(Quote quote1, Quote quote2) {

        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY)) {
            return quote2.getOffer().compareTo(quote1.getOffer());
        }
        if (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) {
            return quote1.getOffer().compareTo(quote2.getOffer());
        }
        if (beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL) {
            //Убывание
            return quote1.getBid().compareTo(quote2.getBid());
        }
        if (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL) {
            //возрастание
            return quote2.getBid().compareTo(quote1.getBid());
        }
        return 0;
    }
}
