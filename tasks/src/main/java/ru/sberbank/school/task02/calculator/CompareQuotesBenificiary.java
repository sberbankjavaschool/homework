package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;

import java.util.Comparator;

public class CompareQuotesBenificiary implements Comparator<QuotePrice> {
    private Beneficiary beneficiary;
    private ClientOperation operation;

    public CompareQuotesBenificiary (ClientOperation operation, Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
        this.operation = operation;
    }

    @Override
    public int compare(QuotePrice quote1, QuotePrice quote2) {
        //Ищем большую цену продажи банком//Offer
        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY)
        || (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL)){
            return quote2.getPrice().compareTo(quote1.getPrice());
        }
        //Ищем меньшую цену покупки банком//bid
        //Ищем меньшую цену продажи банком//offer
        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL) ||
                (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) ) {
            return  quote1.getPrice().compareTo(quote2.getPrice());
        }
        return 0;
    }
}
