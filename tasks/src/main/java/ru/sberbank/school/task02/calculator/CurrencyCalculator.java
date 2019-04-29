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

public class CurrencyCalculator implements FxConversionService {
    private ExternalQuotesService externalQuotesService;
    private List<Quote> quotes = new ArrayList<>();
    private BigDecimal amountOfRequest;
    private String symbolOfRequest;

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        externalQuotesService = new ExternalQuotesServiceDemo();
        quotes = externalQuotesService.getQuotes(symbol);
        this.amountOfRequest = amount;
        this.symbolOfRequest = symbol.getSymbol();
        
        return operation(operation);
    }
    
    private BigDecimal operation(ClientOperation operation) {
        if (operation == ClientOperation.SELL) return findQuote().getOffer();
        if (operation == ClientOperation.BUY) return findQuote().getBid();
        throw new NoSuchElementException();
    }

    private Quote findQuote() {
        BigDecimal amountQuote = null;
        String symbolQuote = "";
        for (Quote quote : quotes) {
            amountQuote = quote.getVolume().getVolume();
            symbolQuote = quote.getSymbol().getSymbol();
            
            if ((symbolQuote.equals(symbolOfRequest) && (amountQuote.compareTo(amountOfRequest) >= 0)))  {
                return quote;
            }
        }
        return null;
    }
}
