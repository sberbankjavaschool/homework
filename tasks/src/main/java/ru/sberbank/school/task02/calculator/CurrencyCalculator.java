package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;

public class CurrencyCalculator implements FxConversionService {
    final ExternalQuotesService externalQuotesService;
    List<Quote> quotes = new ArrayList<>();
    BigDecimal amountOfRequest;
    Symbol symbolOfRequest;

    public CurrencyCalculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {
        quotes = externalQuotesService.getQuotes(Symbol.USD_RUB);
        this.amountOfRequest = amount.setScale(2, BigDecimal.ROUND_FLOOR);
        this.symbolOfRequest = symbol;
        System.out.println("Get request volume: " +  amountOfRequest );
        if (check()) {
            return operation(operation);
        }
        return new BigDecimal(0);
    }

    BigDecimal operation(ClientOperation operation) {
        Optional<Quote> quote = findQuote(new CompareQutes());

        if (quote.isPresent() && operation == ClientOperation.SELL) {
            return quote.get().getBid();
        }
        if (quote.isPresent() && operation == ClientOperation.BUY) {
            return quote.get().getOffer();
        }
        return new BigDecimal(0);
    }

    Optional<Quote> findQuote(Comparator<Quote> comparator) {
        BigDecimal amountQuote;
        List<Quote> sortedQuoteList = filterQutesList(comparator);
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

    List<Quote> filterQutesList(Comparator<Quote> comparator) {
        List<Quote> filterBySymbolList = quotes.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        System.out.println("Sorted List: ");
        showQuotes(filterBySymbolList);
        return filterBySymbolList;
    }

    boolean check() {
        if (quotes.size() == 0) {
            return false;
        }
        if (amountOfRequest.compareTo(new BigDecimal(0)) <= 0) {
            return false;
        }
        if (symbolOfRequest == null)  {
            return false;
        }
        return true;
    }

    void showQuotes(List<Quote> showQutesList) {
        for (Quote quote : showQutesList) {
            System.out.println("Get Quote id: " + quote.getId()
                    + " symbol: " + quote.getSymbol()
                    + " volume: " + quote.getVolume()
                    + " bid: " + quote.getBid()
                    + " offer: " + quote.getOffer());
        }
    }

    void showQuote(Quote quote) {
        System.out.println("Find quote: " + quote.getId()
                + " symbol: " + quote.getSymbol()
                + " volume: " + quote.getVolume()
                + " bid: " + quote.getBid()
                + " offer: " + quote.getOffer());
    }
}
