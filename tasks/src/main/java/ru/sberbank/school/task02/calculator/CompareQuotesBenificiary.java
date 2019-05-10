package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;

import java.math.BigDecimal;
import java.util.Comparator;

public class CompareQuotesBenificiary implements Comparator<Quote> {
    private Beneficiary beneficiary;
    private ClientOperation operation;

    public CompareQuotesBenificiary (ClientOperation operation, Beneficiary beneficiary) {

        this.beneficiary = beneficiary;
        this.operation = operation;
    }

    @Override
    public int compare(Quote quote1, Quote quote2) {
        if (quote1.getVolume().isInfinity()) {
            return 1;
        }
        if (quote2.getVolume().isInfinity()) {
            return -1;
        }
        return compareEqualQuotes(quote1, quote2);
    }

    private int compareEqualQuotes(Quote quote1, Quote quote2) {
        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY) ||
                (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL)) {
                return quote2.getOffer().compareTo(quote1.getOffer());
        }
        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL) ||
                (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY)) {
            return quote2.getBid().compareTo(quote1.getBid());
        }
        return 1;
    }
}
