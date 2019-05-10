package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import ru.sberbank.school.task02.util.*;
import ru.sberbank.school.task02.exception.EmptyQuoteException;

/**
 * Реализация обратного валютного калькулятора.
 */
public class ExtendedCalculator extends Calculator implements ExtendedFxConversionService {

    public ExtendedCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                Beneficiary beneficiary) {

        if (operation == null) {
            throw new NullPointerException("Parameter operation is null!");
        }
        if (symbol == null) {
            throw new NullPointerException("Parameter symbol is null!");
        }
        if (amount == null) {
            throw new NullPointerException("Parameter amount is null!");
        }
        if (beneficiary == null) {
            throw new NullPointerException("Parameter beneficiary is null!");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new EmptyQuoteException("No quotes!");
        }

        List<ReverseQuote> reverseQuotes = getReverseQuotes(quotes, operation);
        List<BigDecimal> prices = new ArrayList<>();

        for (ReverseQuote r : reverseQuotes) {
            if ((r.getVolumeFrom().isInfinity() || amount.compareTo(r.getVolumeFromSize()) >= 0)
                     && (r.getVolumeTo().isInfinity() || amount.compareTo(r.getVolumeToSize()) < 0)) {
                prices.add(r.getPrice());
            }
        }

        if (prices.isEmpty()) {
            return Optional.empty();
        }

        Collections.sort(prices);
        return Optional.of(beneficiary == Beneficiary.CLIENT ^ operation == ClientOperation.BUY
                ? prices.get(prices.size() - 1) : prices.get(0));
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        return Optional.empty();
    }

    private void sortQuotes(List<Quote> quotes) {
        quotes.sort( (q1, q2) -> {
            if (q1.isInfinity()) {
                return 1;
            }
            if (q2.isInfinity()) {
                return -1;
            }
            return q1.getVolumeSize().compareTo(q2.getVolumeSize());
        });
    }

    public List<ReverseQuote> getReverseQuotes(List<Quote> quotes, ClientOperation operation) {

        sortQuotes(quotes);
        List<ReverseQuote> reverseQuotes = new ArrayList<>();

        for (int i = 0; i < quotes.size(); i++) {
            BigDecimal price = operation == ClientOperation.SELL
                    ? quotes.get(i).getBid() : quotes.get(i).getOffer();
            Volume volumeFrom = i == 0
                    ? Volume.from(0) : Volume.from(quotes.get(i - 1).getVolumeSize().multiply(price));
            Volume volumeTo = Volume.from(quotes.get(i).getVolumeSize().multiply(price));
            price = BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);
            ReverseQuote r = new ReverseQuote(volumeFrom, volumeTo, price);
            reverseQuotes.add(r);
        }

        return reverseQuotes;
    }

}
