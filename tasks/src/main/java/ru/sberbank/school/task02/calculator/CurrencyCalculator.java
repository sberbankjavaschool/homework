package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;

public class CurrencyCalculator implements FxConversionService {
    private ExternalQuotesService externalQuotesService;
    private List<Quote> quotes = new ArrayList<>();
    private BigDecimal amountOfRequest;
    private Symbol symbolOfRequest;

    public CurrencyCalculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {

//        externalQuotesService = new ExternalQuotesServiceDemo();
        quotes = externalQuotesService.getQuotes(Symbol.USD_RUB);
        this.amountOfRequest = amount;
        this.symbolOfRequest = symbol;

        if (check())
            return operation(operation);
        return new BigDecimal(0);
    }

    private BigDecimal operation(ClientOperation operation) {
        Optional<Quote> quote = findQuote();
        if (!quote.isPresent()) {
            System.out.println("No quote found");
            return new BigDecimal(0);
        }
        if (operation == ClientOperation.SELL) return quote.get().getOffer();
        if (operation == ClientOperation.BUY) return quote.get().getBid();
        return new BigDecimal(0);
    }

    private Optional<Quote> findQuote() {
        BigDecimal amountQuote;
        List<Quote> sortedQuoteList = filterQutesList();
        for (Quote quote : sortedQuoteList) {
            amountQuote = quote.getVolumeSize();
            if (amountQuote.compareTo(amountOfRequest) >= 0) {
                return Optional.of(quote);
            }
        }
        return Optional.empty();
    }

    public List<Quote> filterQutesList() {
        List<Quote> filterBySymbolList = quotes.stream()
                .filter(p -> p.getVolumeSize().compareTo(new BigDecimal(0)) > 0)
                .filter(p -> p.getSymbol().getSymbol().equals(symbolOfRequest.getSymbol()))
                .sorted(new CompareQutes())
                .collect(Collectors.toList());
        return filterBySymbolList;
    }

    private boolean check() {
        if (quotes.size() == 0) {
            return false;
        }
        if (amountOfRequest.compareTo(new BigDecimal(0)) <= 0) {
            return false;
        }
        if (symbolOfRequest == null)  return false;
        return true;
    }
}
