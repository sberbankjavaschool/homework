package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import java.math.BigDecimal;
import java.util.*;
import java.math.RoundingMode;

public class ExtendedCurrencyCalculator extends CurrencyCalculator implements ExtendedFxConversionService {
    public ExtendedCurrencyCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    private Optional<BigDecimal> extendedOperation(ClientOperation operation,
                                                   Beneficiary beneficiary,
                                                   List<QuotePrice> quotePrices,
                                                   BigDecimal amountOfRequest) {


        Optional<QuotePrice> quotePrice = findQuote(new CompareQuotesBenificiary(operation, beneficiary),
                quotePrices,
                amountOfRequest);

        if (!quotePrice.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(BigDecimal.valueOf(1).divide(quotePrice.get().getPrice(),10, RoundingMode.HALF_UP));
    }

    Optional<QuotePrice> findQuote(Comparator<QuotePrice> comparator,
                              List<QuotePrice> quotePrices,
                              BigDecimal amountOfRequest) {

        List<QuotePrice> finalQuoteList = new ArrayList<>();
        for (QuotePrice quotePrice: quotePrices) {
            if (quotePrice.getAmountInCurr().compareTo(amountOfRequest) > 0
            || quotePrice.getAmountInCurr().compareTo(BigDecimal.ZERO) < 0) {
                finalQuoteList.add(quotePrice);
                System.out.println("Amount " + quotePrice.getAmountInCurr() + " price: " + quotePrice.getPrice());
            }
        }
        finalQuoteList.sort(comparator);
        if (finalQuoteList.size() > 0) {
            System.out.println("Return quote " +  finalQuoteList.get(0).getAmountInCurr() + " with price " +  finalQuoteList.get(0).getPrice());
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
        System.out.println("Get amount " + amount);
        System.out.println("Get operation " + operation );
        System.out.println("Gett benificiary " + beneficiary);
        List<QuotePrice> quotePrices = new ArrayList<>();
        BigDecimal realVolume;
        for (Quote quote: externalQuotesService.getQuotes(symbol)) {
            if (operation == ClientOperation.SELL) {
                realVolume = quote.getVolumeSize().multiply(quote.getBid());
                quotePrices.add(new QuotePrice(realVolume,quote.getBid()));
            } else {
                realVolume = quote.getVolumeSize().multiply(quote.getOffer());
                quotePrices.add(new QuotePrice(realVolume,quote.getOffer()));
            }
            System.out.println("Add quote with real volume " + realVolume);
        }
        return extendedOperation(operation, beneficiary, quotePrices, amount);
    }

}
