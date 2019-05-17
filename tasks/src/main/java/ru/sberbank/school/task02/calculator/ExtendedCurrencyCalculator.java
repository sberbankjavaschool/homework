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

//new CompareQuotesBenificiary(operation, beneficiary)
        Optional<QuotePrice> quotePrice = findQuote(new CompareQutesExtend(),
                quotePrices,
                amountOfRequest);

        if (!quotePrice.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(quotePrice.get().getPricePerPiece());
    }

    Optional<QuotePrice> findQuote(Comparator<QuotePrice> comparator,
                              List<QuotePrice> quotePrices,
                              BigDecimal amountOfRequest) {
        BigDecimal priceInCurr;
        List<QuotePrice> finalQuoteList = new ArrayList<>();
        for (QuotePrice quotePrice: quotePrices) {
            priceInCurr  = amountOfRequest.divide(quotePrice.getPrice(), 10, RoundingMode.HALF_UP);
            if (quotePrice.getAmount().compareTo(priceInCurr) > 0
            || quotePrice.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                finalQuoteList.add(quotePrice);
                System.out.println("add to list quote: " + quotePrice.toString());
            }
        }
        finalQuoteList.sort(comparator);
        if (finalQuoteList.size() > 0) {
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
        System.out.println("Get amount " + amount);
        System.out.println("Get operation " + operation );
        System.out.println("Gett benificiary " + beneficiary);

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

}
