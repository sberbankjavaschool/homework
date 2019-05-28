package ru.sberbank.school.task02;

import javafx.util.Pair;
import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.*;

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
    private Pair<ReversedQuote, ReversedQuote> findClosest(@NonNull List<Quote> quotes,
                                                           @NonNull ClientOperation direction,
                                                           @NonNull BigDecimal amount) {

        /*ReversedQuote left = new ReversedQuote(
                Quote
                        .builder()
                        .symbol(quotes.get(0).getSymbol())
                        .volume(Volume.INFINITY)
                        .bid(BigDecimal.ONE)
                        .offer(BigDecimal.ONE)
                        .build(),
                direction,
                Volume.INFINITY);

        ReversedQuote right = new ReversedQuote(
                Quote
                        .builder()
                        .symbol(quotes.get(0).getSymbol())
                        .volume(Volume.INFINITY)
                        .bid(BigDecimal.ONE)
                        .offer(BigDecimal.ONE)
                        .build(),
                direction,
                Volume.INFINITY);*/
        Iterator<Quote> iter = quotes.iterator();
        Position currentPos;

        ReversedQuote left = new ReversedQuote(iter.next(), direction);
        ReversedQuote right = new ReversedQuote(iter.next(), direction);
        ReversedQuote current;

        Volume leftVolume = left.getQ().getVolume();
        Volume rightVolume = right.getQ().getVolume();
        Volume currentVolume;

        if (compare(rightVolume, leftVolume) < 0) {

            current = right;
            right = left;

            currentVolume = rightVolume;
            rightVolume = leftVolume;
            leftVolume = currentVolume;
            left = current;
        }

        right.setLowerBound(leftVolume);

        while (iter.hasNext()) {

            current = new ReversedQuote(iter.next(), direction);
            currentVolume = current.getQ().getVolume();

            if (compare(currentVolume, leftVolume) >= 0 &&
                compare(currentVolume, rightVolume) <= 0) {

                current.setLowerBound(leftVolume);
                right.setLowerBound(currentVolume);

                currentPos = checkBounds(left, right, amount);

                if (currentPos == Position.OUTOFBOUNDS ||
                    currentPos == Position.BETWEENINTERSEC) {

                    right = current;
                    rightVolume = currentVolume;
                    left = current;
                    leftVolume = currentVolume;
                } else if (currentPos == Position.INSIDELOWER) {

                    right = current;
                    rightVolume = currentVolume;
                } else if (currentPos == Position.INSIDEUPPER) {

                    left = current;
                    leftVolume = currentVolume;
                }

            } else if (compare(currentVolume, leftVolume) <= 0) {

                currentPos = checkBounds(current, current, amount);
                left.setLowerBound(currentVolume);

                if (currentPos == Position.INSIDELOWER ||
                    currentPos == Position.BETWEENINTERSEC) {

                    right = left;
                    rightVolume = leftVolume;
                    left = current;
                    leftVolume = currentVolume;
                }

            } else if (compare(currentVolume, rightVolume) >= 0) {

                currentPos = checkBounds(current, current, amount);
                current.setLowerBound(rightVolume);

                if (currentPos == Position.INSIDEUPPER ||
                    currentPos == Position.BETWEENINTERSEC) {

                    left = right;
                    leftVolume = rightVolume;
                    right = current;
                    rightVolume = currentVolume;
                }
            }
        }

        return new Pair<>(left, right);
    }

    private Optional<BigDecimal> getPrice(ReversedQuote lower, ReversedQuote upper, double delta,
                                          BigDecimal amount, Beneficiary beneficiary) {

        BigDecimal price = pickPrice(lower, upper, beneficiary, checkBounds(lower, upper, amount));

        /*if(price == null && Math.abs(delta - 0) > 1e-5) {

            Position pos = checkBounds(lower, upper, amount.add(BigDecimal.valueOf(delta)),
                                                     amount.subtract(BigDecimal.valueOf(delta)));

            price = pickPrice(lower, upper, beneficiary, pos);
        }*/

        return price == null ? Optional.empty() : Optional.of(price);
    }

    /**
     * Checks if amount is inside of one or more
     * of its neighboring volumes' bounds.
     * @param ub Right closest volume's lower bound.
     * @param lb Left closest volume's upper bound.
     * @param amount Amount
     * @return relative position of amount.
     */
    private Position checkBounds(ReversedQuote lb, ReversedQuote ub, BigDecimal amount) {

        Volume lower = lb.getUpperBound();
        Volume upper = ub.getLowerBound();
        Volume a = Volume.from(amount);

        SortedSet<Volume> bounds = new TreeSet<>(this::compare);

        bounds.add(lower);
        bounds.add(upper);
        bounds.add(a);

        if (bounds.first().equals(upper) && bounds.last().equals(lower)) {
            return Position.BETWEENINTERSEC;
        } else if (bounds.first().equals(a)) {
            return Position.INSIDELOWER;
        } else if (bounds.last().equals(a)) {
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

    private int compare(Volume v1, Volume v2){

        if (v1.isInfinity()) {
            return 1;
        }
        if (v2.isInfinity()) {
            return -1;
        }
        return v1.getVolume().compareTo(v2.getVolume());
    }
}
