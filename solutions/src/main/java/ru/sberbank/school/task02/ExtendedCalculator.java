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
        return Optional.of(prices.get(beneficiary == Beneficiary.CLIENT ^ operation == ClientOperation.BUY
                ? prices.size() - 1 : 0));
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

    private List<ReverseQuote> getReverseQuotes(List<Quote> quotes, ClientOperation operation) {

        sortQuotes(quotes);
        List<ReverseQuote> reverseQuotes = new ArrayList<>();

        Iterator<Quote> iterator = quotes.iterator();
        Quote qPrev = null;
        Quote q = null;

        while (iterator.hasNext()) {
            qPrev = q;
            q = iterator.next();

            BigDecimal price = operation == ClientOperation.SELL
                    ? q.getBid() : q.getOffer();
            Volume volumeFrom = qPrev == null
                    ? Volume.from(0) : Volume.from(qPrev.getVolumeSize().multiply(price));
            Volume volumeTo = Volume.from(q.getVolumeSize().multiply(price));
            price = BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);
            ReverseQuote r = new ReverseQuote(volumeFrom, volumeTo, price);
            reverseQuotes.add(r);
        }

        return reverseQuotes;
    }

}

