package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import java.math.BigDecimal;
import java.util.*;

public class ExtendedCurrencyCalculator extends CurrencyCalculator implements ExtendedFxConversionService {
    private final ExternalQuotesService externalQuotesService;

    public ExtendedCurrencyCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
        this.externalQuotesService = externalQuotesService;
    }

    private Optional<BigDecimal> extendedOperation(ClientOperation operation,
                                                   Beneficiary beneficiary,
                                                   List<QuotePrice> quotePrices,
                                                   BigDecimal amountOfRequest) {

        Optional<QuotePrice> quotePrice = findQuote(new CompareQutesExtend(),
                quotePrices,
                amountOfRequest, new CompareQuotesBenificiary(operation, beneficiary));

        if (!quotePrice.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(quotePrice.get().getPricePerPiece());
    }

    Optional<QuotePrice> findQuote(Comparator<QuotePrice> compareByAmount,
                              List<QuotePrice> quotePrices,
                              BigDecimal amountOfRequest,
                              Comparator<QuotePrice> compareByBenificiary) {

        List<QuotePrice> finalQuoteList = new ArrayList<>();
        quotePrices.sort(compareByAmount);
        BigDecimal lowerLimit = BigDecimal.ZERO;

        for (QuotePrice quotePrice: quotePrices) {
            quotePrice.setLowerLimit(lowerLimit);
            if (quotePrice.checkQuoteInMyScope(amountOfRequest)) {
               finalQuoteList.add(quotePrice);
            }
            lowerLimit = quotePrice.getUpperLimit();
        }
        if (finalQuoteList.size() > 0) {
            finalQuoteList.sort(compareByBenificiary);
            System.out.println("Return quote: " + finalQuoteList.get(0).toString());
            return Optional.of(finalQuoteList.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        if (operation == null) {
            throw new NullPointerException("operation is null!");
        }
        if (beneficiary == null) {
            throw new NullPointerException("beneficiary is null!");
        }
        printTask(amount, operation, beneficiary);
        List<QuotePrice> quotePrices = new ArrayList<>();
        for (Quote quote: externalQuotesService.getQuotes(symbol)) {
            if (operation == ClientOperation.SELL) {
                quotePrices.add(new QuotePrice(quote.getVolumeSize(),quote.getBid()));
            } else {
                quotePrices.add(new QuotePrice(quote.getVolumeSize(),quote.getOffer()));
            }
        }
        return extendedOperation(operation, beneficiary, quotePrices, amount);
    }

    public void printTask(BigDecimal amount, ClientOperation operation, Beneficiary beneficiary) {
        System.out.println("Get amount " + amount);
        System.out.println("Get operation " + operation );
        System.out.println("Gett benificiary " + beneficiary);
    }

}
