package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.math.RoundingMode;

public class ExtendedCurrencyCalculator extends CurrencyCalculator implements ExtendedFxConversionService {
    private int rounding_mode;

    public ExtendedCurrencyCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    private Optional<BigDecimal> extendedOperation(ClientOperation operation, Beneficiary beneficiary, List<Quote> quotes) {
        Optional<Quote> quote = findQuote(new CompareQuotesBenificiary(operation, beneficiary), quotes, operation);
        if (!quote.isPresent()) {
            System.out.println("Return Optional.empty()");
            return Optional.empty();
        }
        showQuote(quote.get());
        if (operation == ClientOperation.SELL) {
                System.out.println("Returl value: " + BigDecimal.valueOf(1).divide(quote.get().getBid(),10, RoundingMode.HALF_UP));
                return Optional.of(BigDecimal.valueOf(1).divide(quote.get().getBid(),10, RoundingMode.HALF_UP));
        }
        if (operation == ClientOperation.BUY) {
            System.out.println("Returl value: " + BigDecimal.valueOf(1).divide(quote.get().getOffer(),10, RoundingMode.HALF_UP));
            return Optional.of(BigDecimal.valueOf(1).divide(quote.get().getOffer(), 10, RoundingMode.HALF_UP));
        }
        System.out.println("Return Optional.empty()");
        return Optional.empty();
    }

    Optional<Quote> findQuote(Comparator<Quote> comparator, List<Quote> quotes, ClientOperation operation) {
        List<Quote> finalQuoteList = new ArrayList<>();
        BigDecimal countCurr, volumeInCurr;
        for (Quote quote: quotes) {
            if (operation == ClientOperation.SELL) {
                countCurr = quote.getBid();
            } else {
                countCurr = quote.getOffer();
            }
            volumeInCurr = quote.getVolumeSize().multiply(countCurr).setScale(10, RoundingMode.HALF_UP);
            if (quote.getVolume().isInfinity() || volumeInCurr.compareTo(amountOfRequest) > 0) {
                finalQuoteList.add(quote);
                System.out.println("add quote to list with value: " + quote.getVolumeSize());
            }
        }
        if (finalQuoteList.size() > 0) {
            finalQuoteList.sort(comparator);
            System.out.println("Sorted list: ");
            showQuotes(finalQuoteList);
            return Optional.of(finalQuoteList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        quotes = externalQuotesService.getQuotes(Symbol.USD_RUB);

        if (operation == null) {
            throw new NullPointerException("operation is null!");
        }
        if (beneficiary == null) {
            throw new NullPointerException("beneficiary is null!");
        }

        this.symbolOfRequest = symbol;
        this.amountOfRequest = amount;
        System.out.println("Get benificiary: " + beneficiary.toString());
        System.out.println("Get symbol: " + symbol.getSymbol());
        System.out.println("Get client operation: " + operation.toString());
        System.out.println("Get request volume: " +  amountOfRequest );
        showQuotes(quotes);
//        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY)
//                || (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL)) {
//            rounding_mode = RoundingMode.HALF_UP;
//        }
//        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL)
//                || (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY)) {
//            rounding_mode = BigDecimal.HALF_UP;
//        }
        this.amountOfRequest = amount.setScale(10,  rounding_mode);

        if (check()) {
            return extendedOperation(operation, beneficiary, quotes);
        }
        System.out.println("Return Optional.empty() after check");
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                Symbol symbol,
                                                BigDecimal amount,
                                                double delta,
                                                Beneficiary beneficiary) {
         return Optional.empty();
    }
}
