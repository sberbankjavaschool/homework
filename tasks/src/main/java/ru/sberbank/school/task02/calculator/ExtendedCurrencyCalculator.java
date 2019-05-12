package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.math.RoundingMode;
import java.util.stream.Collectors;

public class ExtendedCurrencyCalculator extends CurrencyCalculator implements ExtendedFxConversionService {
    private int rounding_mode;

    public ExtendedCurrencyCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    private Optional<BigDecimal> extendedOperation(ClientOperation operation, Beneficiary beneficiary) {
        Optional<Quote> quote = findQuote(new CompareQuotesByPrice(operation, beneficiary), quotes);
        if (!quote.isPresent()) {
            System.out.println("Return Optional.empty()");
            return Optional.empty();
        }
        showQuote(quote.get());
        if (operation == ClientOperation.SELL) {
                System.out.println("Returl value: " + BigDecimal.valueOf(1).divide(quote.get().getBid(),10, rounding_mode));
                return Optional.of(BigDecimal.valueOf(1).divide(quote.get().getBid(),10, RoundingMode.HALF_UP));
        }
        if (operation == ClientOperation.BUY) {
            System.out.println("Returl value: " + BigDecimal.valueOf(1).divide(quote.get().getOffer(),10, rounding_mode));
            return Optional.of(BigDecimal.valueOf(1).divide(quote.get().getOffer(), 10, RoundingMode.HALF_UP));
        }
        System.out.println("Return Optional.empty()");
        return Optional.empty();
    }

    Optional<Quote> findQuote(Comparator<Quote> comparator, List<Quote> quotes) {
        BigDecimal amountQuote;
        List<Quote> finalQuoteList = new ArrayList<>();
        for (Quote quote: quotes) {
            if (quote.getVolume().isInfinity() || quote.getVolumeSize().compareTo(amountOfRequest) > 0) {
                finalQuoteList.add(quote);
            }
        }

        List<Quote> sortedQuoteList = filterQutesList(comparator, finalQuoteList);
        for (Quote quote : sortedQuoteList) {
            amountQuote = quote.getVolumeSize();
            if (amountQuote.compareTo(amountOfRequest) > 0) {
                return Optional.of(quote);
            }
            if (quote.getVolume().isInfinity()) {
                return Optional.of(quote);
            }
        }
        return Optional.empty();
    }

    List<Quote> filterQutesList(Comparator<Quote> comparator, List<Quote> quotes) {
        List<Quote> filterBySymbolList = quotes.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        System.out.println("Sorted List: ");
        showQuotes(filterBySymbolList);
        return filterBySymbolList;
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
            return extendedOperation(operation, beneficiary);
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
