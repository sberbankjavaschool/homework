package ru.sberbank.school.task02;

import javafx.util.Pair;
import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ReversedCurrencyCalc extends CurrencyCalc implements ExtendedFxConversionService {

    public ReversedCurrencyCalc(ExternalQuotesService externalQuotes) {
        super(externalQuotes);
    }

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation,
                                                @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount,
                                                @NonNull Beneficiary beneficiary) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount should be positive");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("no available quotes");
        }

        Pair<ReversedQuote, ReversedQuote> bounds = findBounds(quotes, operation, amount);

        return getPrice(bounds.getKey(), bounds.getValue(), amount, beneficiary);
    }

    private Pair<ReversedQuote, ReversedQuote> findBounds(@NonNull List<Quote> quotes, ClientOperation direction, BigDecimal amount) {

        Quote upper = Quote.builder()
                .symbol(quotes.get(0).getSymbol())
                .volume(Volume.INFINITY)
                .bid(BigDecimal.ZERO)
                .offer(BigDecimal.ZERO)
                .build();
        Quote lower = upper;

        for (Quote quote : quotes) {

            BigDecimal price = direction == ClientOperation.BUY
                            ? quote.getBid()
                            : quote.getOffer();
            BigDecimal currentVolume = quote.getVolumeSize();

            if (upper.isInfinity() && quote.isInfinity()) {
                upper = quote;
            }

            if(currentVolume.multiply(price).compareTo(amount) >= 0) {

                if (upper.isInfinity() || upper.getVolumeSize().compareTo(currentVolume) > 0) {
                    upper = quote;
                }
            } else if (lower.getVolumeSize().compareTo(currentVolume) < 0) {
                lower = quote;
            }
        }

        ReversedQuote low = new ReversedQuote(lower, direction, Volume.INFINITY);
        ReversedQuote up = new ReversedQuote(upper, direction, lower.getVolume());

        return new Pair<>(low, up);
    }

    private Optional<BigDecimal> getPrice(ReversedQuote upper, ReversedQuote lower,
                                          BigDecimal amount, Beneficiary beneficiary) {

        BigDecimal lb = lower.getUpperBound().getVolume();
        BigDecimal ub = upper.getLowerBound().getVolume();

        if (lb.compareTo(amount) >= 0 && ub.compareTo(amount) <= 0) {

            boolean bigger = beneficiary == Beneficiary.BANK ^ upper.getDirection() == ClientOperation.BUY;

            BigDecimal price = bigger
                    ? lower.getPrice()
                    : upper.getPrice();

            return Optional.of(price);
        } else if (lb.compareTo(amount) >= 0) {
            return Optional.of(lower.getPrice());
        } else if (ub.compareTo(amount) <= 0) {
            return Optional.of(upper.getPrice());
        }

        /*if(delta != 0) {

            BigDecimal plusDelta = amount.plus(BigDecimal.valueOf(delta));
            BigDecimal minusDelta = amount.plus(BigDecimal.valueOf(-delta));


        }*/

        return Optional.empty();
    }


}
