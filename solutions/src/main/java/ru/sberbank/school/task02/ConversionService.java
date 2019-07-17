package ru.sberbank.school.task02;

import lombok.Getter;
import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ConversionService implements FxConversionService {

    private ExternalQuotesService externalQuotesService;

    public ConversionService(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        Quote quote = getQuote(symbol, amount);
        return getPrice(operation, quote);
    }

    private Quote getQuote(Symbol symbol, BigDecimal amount) {
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("Нет квот");
        }
        return getNearQuoteOfVolume(quotes, amount);
    }

    protected Quote getNearQuoteOfVolume(List<Quote> quotes, BigDecimal amount) {
        Quote nearQuote = null;
        Quote infQuote = null;
        int i;
        for (i = 0; i < quotes.size(); i++) {
            if (quotes.get(i).isInfinity()) {
                infQuote = quotes.get(i);
                continue;
            }
            if (amount.compareTo(quotes.get(i).getVolumeSize()) >= 0) {
                continue;
            } else {
                nearQuote = quotes.get(i);
                i++;
                break;
            }
        }
        for (int j = i; j < quotes.size(); j++) {
            if (quotes.get(j).isInfinity()) {
                infQuote = quotes.get(j);
                continue;
            }
            if (amount.compareTo(quotes.get(j).getVolumeSize()) >= 0) {
                continue;
            } else {
                if (nearQuote.getVolumeSize().compareTo(quotes.get(j).getVolumeSize()) > 0) {
                    nearQuote = quotes.get(j);
                }
            }
        }
        return (nearQuote == null) ? infQuote : nearQuote;
    }

    protected BigDecimal getPrice(ClientOperation operation, Quote quote) {
        switch (operation) {
            case BUY:
                return quote.getOffer();
            case SELL:
                return quote.getBid();
            default:
                return null;
        }
    }
}
