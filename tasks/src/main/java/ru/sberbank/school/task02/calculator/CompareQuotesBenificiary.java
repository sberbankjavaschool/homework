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
        //Ищем большую цену продажи банком
        if (beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY) {
            return quote2.getOffer().compareTo(quote1.getOffer());
        }
        //Ищем меньшую цену покупки банком
        if (beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL) {
            return  quote1.getBid().compareTo(quote2.getBid());
        }
        //Ищем большую цену покупки банком
        if (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL) {
            return quote2.getBid().compareTo(quote1.getBid());
        }
        //Ищем меньшую цену продажи банком
        if (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) {
            return quote1.getOffer().compareTo(quote2.getOffer());
        }
        return 0;
    }
}
