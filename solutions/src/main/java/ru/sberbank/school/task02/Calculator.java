package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class Calculator implements FxConversionService {
    private ExternalQuotesService provider;

    Calculator(ExternalQuotesService externalQuotesService) {
        this.provider = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0
                || operation == null || symbol == null) {
            throw new ConverterConfigurationException("Wrong parameters");
        }
        List<Quote> quotes = provider.getQuotes(symbol);
        if (quotes.isEmpty()) {
            throw new FxConversionException("No quotes found");
        }

        Quote current = null;
        for (Quote quote: quotes) {
            current = checkQuote(amount, quote, current);
        }
        if (current == null) {
            return null;
        }
        return operation == ClientOperation.BUY ? current.getOffer() : current.getBid();
    }

    Quote checkQuote(BigDecimal amount, Quote quote, Quote current) {
        if (quote.isInfinity() && current == null) {
            current = quote;
        } else if (amount.compareTo(quote.getVolumeSize()) < 0) {
            if (current == null || current.isInfinity()
                    || current.getVolumeSize().compareTo(quote.getVolumeSize()) > 0) {
                current = quote;
            }
        }
        return current;
    }
}
