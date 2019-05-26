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

        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation,
                                                @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount,
                                                double delta,
                                                @NonNull Beneficiary beneficiary) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount should be positive");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("no available quotes");
        }

        Pair<ReversedQuote, ReversedQuote> neighbors = findClosest(quotes, operation, amount);

        return getPrice(neighbors.getKey(), neighbors.getValue(), delta, amount, beneficiary);
    }

    /**
     * This function picks two quotes with volumes
     * that have bounds closest to amount.
     * @param quotes List of available quotes.
     * @param direction Operation type.
     * @param amount Amount in target currency
     * @return Pair of reversed quotes wich are made
     *          from two suitable quotes.
     */
    private Pair<ReversedQuote, ReversedQuote> findClosest(List<Quote> quotes,
                                                           ClientOperation direction,
                                                           BigDecimal amount) {

        Quote upper = Quote.builder()
                .symbol(quotes.get(0).getSymbol())
                .volume(Volume.INFINITY)
                .bid(BigDecimal.ZERO)
                .offer(BigDecimal.ZERO)
                .build();
        Quote lower = upper;

        for (Quote quote : quotes) {

            BigDecimal price = direction == ClientOperation.BUY ? quote.getBid() : quote.getOffer();
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

    private Optional<BigDecimal> getPrice(ReversedQuote upper, ReversedQuote lower, double delta,
                                          BigDecimal amount, Beneficiary beneficiary) {

        BigDecimal lb = lower.getUpperBound().getVolume();
        BigDecimal ub = upper.getLowerBound().getVolume();

        BigDecimal price = pickPrice(lower, upper, beneficiary, checkBounds(ub, lb, amount, amount));

        if(price == null && Math.abs(delta - 0) > 1e-5) {
             price = pickPrice(lower, upper, beneficiary,
                     checkBounds(ub, lb, amount.add(BigDecimal.valueOf(delta)),
                                         amount.subtract(BigDecimal.valueOf(delta))));
        }

        return price == null ? Optional.empty() : Optional.of(price);
    }

    /**
     * Checks if amount is inside one or more of its neighboring
     * volumes' bounds.
     * @param ub Right closest volume's lower bound.
     * @param lb Left closest volume's upper bound.
     * @param plusDelta Amount plus delta.
     * @param minusDelta Amount minus delta.
     * @return relative position of amount.
     */
    private Position checkBounds(BigDecimal ub, BigDecimal lb, BigDecimal plusDelta,
                                   BigDecimal minusDelta) {

        if (lb.compareTo(minusDelta) >= 0 && ub.compareTo(plusDelta) <= 0) {
            return Position.BETWEENINTERSEC;
        } else if (lb.compareTo(minusDelta) >= 0) {
            return Position.INSIDELOWER;
        } else if (ub.compareTo(plusDelta) <= 0) {
            return Position.INSIDEUPPER;
        }

        return Position.OUTOFBOUNDS;
    }

    /**
     * Picks price based on amount's position,
     * beneficiary and operation type.
     */
    private BigDecimal pickPrice(ReversedQuote lower, ReversedQuote upper,
                                 Beneficiary beneficiary, Position pos) {

        /*
         * BANK && SELL - lower price
         * BANK && BUY - higher price
         * CLIENT && SELL - higher price
         * CLIENT && BUY - lower price
         */
        boolean max = beneficiary == Beneficiary.BANK ^ upper.getDirection() == ClientOperation.SELL;

        switch (pos) {

            case BETWEENINTERSEC:
                return max ? upper.getPrice() : lower.getPrice();
            case INSIDELOWER:
                return lower.getPrice();
            case INSIDEUPPER:
                return upper.getPrice();
            case OUTOFBOUNDS:
            default:
                return null;
        }
    }

    /**
     * Describes amount position relative to
     * bounding volumes' values.
     */

    private enum Position {

        /**
         * Amount lies on the intersection of two volumes.
         */
        BETWEENINTERSEC,

        /**
         * Amount is below it's left closest volume's upper bound.
         */
        INSIDELOWER,

        /**
         * Amount is above it's right closest volume's lower bound.
         */
        INSIDEUPPER,

        /**
         * Amount isn't covered by any volume's bounds
         */
        OUTOFBOUNDS
    }
}
