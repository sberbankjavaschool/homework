package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

public class CurrencyCalculator implements FxConversionService {
    private final ExternalQuotesService externalQuotesService;

    public CurrencyCalculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {
        System.out.println("Get request volume: " +  amount );
        return operation(operation, symbol, amount);
    }

    private BigDecimal operation(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = filterQutesList(externalQuotesService.getQuotes(symbol), amount);
        if (quotes.size() > 0 && operation == ClientOperation.SELL) {
            return quotes.get(0).getBid();
        }
        if (quotes.size() > 0 && operation == ClientOperation.BUY) {
            return quotes.get(0).getOffer();
        }
        return BigDecimal.ZERO;
    }

    private List<Quote> filterQutesList(List<Quote> quotes, BigDecimal amountOfRequest) {
        showQuotes(quotes);
        quotes.sort(new CompareQutes());
        List<Quote> filteredList = new ArrayList<>();
        for (Quote quote : quotes) {
            if (quote.getVolumeSize().compareTo(amountOfRequest) > 0
            || quote.getVolume().isInfinity()) {
                filteredList.add(quote);
            }
        }
        return filteredList;
    }

    void showQuotes(List<Quote> quotes) {
        for (Quote quote : quotes) {
            System.out.println("Get Quote symbol: " + quote.getSymbol()
                    + " volume: " + quote.getVolume()
                    + " bid: " + quote.getBid()
                    + " offer: " + quote.getOffer());
        }
    }
}
