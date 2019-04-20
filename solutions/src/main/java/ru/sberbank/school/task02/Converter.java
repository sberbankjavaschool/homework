package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {

    private ExternalQuotesService service;

    public Converter(ExternalQuotesService service) {
        this.service = service;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = service.getQuotes(symbol);
        quotes.sort((o1, o2) -> {
            if (o1.isInfinity()) {
                return 1;
            }
            if (o2.isInfinity()) {
                return 0;
            }
            return o1.getVolumeSize().compareTo(o2.getVolumeSize());
        });
        BigDecimal lastVolume = BigDecimal.ZERO;
        Quote matchedQuote = null;
        for (Quote quote : quotes) {
            if (amount.compareTo(lastVolume) > 0) {
                if (amount.compareTo(quote.getVolumeSize()) < 0) {
                    matchedQuote = quote;
                }
            }
        }
        return operation == ClientOperation.SELL ? matchedQuote.getBid() : matchedQuote.getOffer();
    }
}
