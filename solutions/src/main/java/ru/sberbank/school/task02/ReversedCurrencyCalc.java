package ru.sberbank.school.task02;

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

        SortedSet<ReversedQuote> prices = findPrices(quotes, operation, amount);

        return getPrice(prices, beneficiary, operation);
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
    private SortedSet<ReversedQuote> findPrices(@NonNull List<Quote> quotes,
                                                 @NonNull ClientOperation direction,
                                                 @NonNull BigDecimal amount) {

        SortedSet<ReversedQuote> prices = new TreeSet<>(Comparator.comparing(ReversedQuote::getPrice));
        Iterator<Quote> iter = quotes.iterator();

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
        Position currentPos = checkBounds(left.getLowerBound(), left.getUpperBound(),
                                          right.getLowerBound(), right.getUpperBound(), amount);

        if (currentPos == Position.INTERSECTION) {

            prices.add(left);
            prices.add(right);
        } else if (currentPos == Position.LEFT) {
            prices.add(left);
        } else if (currentPos == Position.RIGHT) {
            prices.add(right);
        }

        while (iter.hasNext()) {

            current = new ReversedQuote(iter.next(), direction);
            currentVolume = current.getQ().getVolume();

            if (compare(currentVolume, leftVolume) >= 0 && compare(currentVolume, rightVolume) <= 0) {

                current.setLowerBound(leftVolume);
                right.setLowerBound(currentVolume);

                currentPos = checkBounds(left.getLowerBound(), left.getUpperBound(),
                                         right.getLowerBound(), right.getUpperBound(), amount);

                if (currentPos == Position.MIDDLE) {

                    prices.remove(right);
                    prices.remove(left);
                    currentPos = checkBounds(current.getLowerBound(), current.getUpperBound(), amount);

                    if (currentPos == Position.INTERSECTION) {

                        prices.add(current);
                        left = current;
                        leftVolume = currentVolume;
                    } else if (currentPos == Position.BOTTOM) {

                        left = current;
                        leftVolume = currentVolume;
                    } else if (currentPos == Position.TOP) {

                        right = current;
                        rightVolume = currentVolume;
                    }
                } else if (currentPos == Position.LEFT) {

                    prices.remove(right);
                    currentPos = checkBounds(current.getLowerBound(), current.getUpperBound(), amount);

                    right = current;
                    rightVolume = currentVolume;

                    if (currentPos == Position.INTERSECTION) {
                        prices.add(current);
                    }
                } else if (currentPos == Position.RIGHT) {

                    prices.remove(left);
                    currentPos = checkBounds(current.getLowerBound(), current.getUpperBound(), amount);

                    left = current;
                    leftVolume = currentVolume;


                    if (currentPos == Position.INTERSECTION) {
                        prices.add(current);
                    }
                }

            } else if (compare(currentVolume, leftVolume) <= 0) {

                left.setLowerBound(currentVolume);
                currentPos = checkBounds(left.getLowerBound(), left.getUpperBound(), amount);

                if (currentPos == Position.BOTTOM) {

                    prices.remove(left);
                    currentPos = checkBounds(current.getLowerBound(), current.getUpperBound(), amount);

                    right = left;
                    rightVolume = leftVolume;
                    left = current;
                    leftVolume = currentVolume;

                    if (currentPos == Position.INTERSECTION) {
                        prices.add(current);
                    }
                }

            } else if (compare(currentVolume, rightVolume) >= 0) {

                current.setLowerBound(rightVolume);
                currentPos = checkBounds(right.getLowerBound(), right.getUpperBound(), amount);

                if (currentPos == Position.TOP) {

                    prices.remove(right);
                    currentPos = checkBounds(current.getLowerBound(), current.getUpperBound(), amount);

                    left = right;
                    leftVolume = rightVolume;
                    right = current;
                    rightVolume = currentVolume;

                    if (currentPos == Position.INTERSECTION) {
                        prices.add(current);
                    }
                }
            }
        }

        return prices;
    }

    private Optional<BigDecimal> getPrice(SortedSet<ReversedQuote> prices,
                                          Beneficiary beneficiary,
                                          ClientOperation direction) {

        if (prices.isEmpty()) {
            return Optional.empty();
        }
        if (prices.size() == 1) {
            return Optional.of(prices.first().getPrice());
        }

        boolean isBigger = beneficiary == Beneficiary.BANK ^ direction == ClientOperation.SELL;

        /*if(price == null && Math.abs(delta - 0) > 1e-5) {

            Position pos = checkBounds(lower, upper, amount.add(BigDecimal.valueOf(delta)),
                                                     amount.subtract(BigDecimal.valueOf(delta)));

            price = pickPrice(lower, upper, beneficiary, pos);
        }*/

        return isBigger ? Optional.of(prices.first().getPrice())
                        : Optional.of(prices.last().getPrice());
    }

    /**
     * Checks if amount is inside of one or more
     * of its neighboring volumes' bounds.
     * @return relative position of amount.
     */
    private Position checkBounds(Volume lowerLeft, Volume lowerRight,
                                 Volume upperLeft, Volume upperRight,
                                 BigDecimal amount) {
        
        Volume a = Volume.from(amount);

        TreeSet<Volume> bounds = new TreeSet<>(this::compare);

        bounds.add(lowerLeft);
        bounds.add(lowerRight);
        bounds.add(upperLeft);
        bounds.add(upperRight);
        bounds.add(a);

        if (bounds.first().equals(a)) {
            return Position.BOTTOM;
        }
        if (bounds.last().equals(a)) {
            return Position.TOP;
        }
        if (lowerLeft.equals(bounds.lower(a)) && lowerRight.equals(bounds.higher(a))) {
            return Position.LEFT;
        }
        if (upperLeft.equals(bounds.lower(a)) && upperRight.equals(bounds.higher(a))) {
            return Position.RIGHT;
        }
        if (upperLeft.equals(bounds.lower(a)) && lowerRight.equals(bounds.higher(a))) {
            return Position.INTERSECTION;
        }

        return Position.MIDDLE;
    }

    private Position checkBounds(Volume left, Volume right, BigDecimal amount) {

        Volume a = Volume.from(amount);

        if (compare(a, left) < 0) {
            return Position.BOTTOM;
        }
        if (compare(a, right) > 0) {
            return Position.TOP;
        }

        return Position.INTERSECTION;
    }


    /**
     * Describes amount position relative to
     * bounding volumes' values.
     */
    private enum Position {

        /**
         * Amount lies between two intersecting bounds.
         */
        INTERSECTION,

        /**
         * Amount is inside lower bounds.
         */
        LEFT,

        /**
         * Amount is inside upper bounds.
         */
        RIGHT,

        /**
         * Amount is between two non-intersecting bounds.
         */
        MIDDLE,

        /**
         * Amount is between upper bound and inf.
         */
        TOP,

        /**
         * Amount is between lower bound and zero.
         */
        BOTTOM
    }

    private int compare(Volume v1, Volume v2) {

        if (v1.isInfinity()) {
            return 1;
        }
        if (v2.isInfinity()) {
            return -1;
        }
        return v1.getVolume().compareTo(v2.getVolume());
    }
}
