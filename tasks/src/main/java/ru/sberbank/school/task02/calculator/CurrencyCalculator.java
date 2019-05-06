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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;

public class CurrencyCalculator implements FxConversionService {
    private ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();
    private List<Quote> quotes = new ArrayList<>();
    private BigDecimal amountOfRequest;
    private Symbol symbolOfRequest;

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
        if (!quote.isPresent()) return new BigDecimal(0);
        if (operation == ClientOperation.SELL) return quote.get().getOffer();
        if (operation == ClientOperation.BUY) return quote.get().getBid();
        return new BigDecimal(0);
    }

    private Optional<Quote> findQuote() {
        BigDecimal amountQuote = null;
        String symbolQuote = "";
        for (Quote quote : quotes) {
            amountQuote = quote.getVolumeSize();
            symbolQuote = quote.getSymbol().getSymbol();
            if ((symbolQuote.equals(symbolOfRequest) && (amountQuote.compareTo(amountOfRequest) >= 0)))  {
                return Optional.of(quote);
            }
        }
        return Optional.empty();
    }

    public List<Quote> filterQutesList() {
        quotes = externalQuotesService.getQuotes(symbolOfRequest);
        List<Quote> filterBySymbolList = quotes.stream()
                .filter(p -> p.getVolumeSize().compareTo(new BigDecimal(0)) > 0)
                .filter(p -> p.getSymbol().getSymbol().equals("USD/RUB"))
                .collect(Collectors.toList());

        filterBySymbolList = filterBySymbolList.stream()
                .sorted(new CompareQutes()).collect(Collectors.toList());
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
