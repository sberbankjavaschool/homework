package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExtendedCurrencyCalc extends CurrencyCalculator implements ExtendedFxConversionService {
    private ExternalQuotesService externalQuotesService;

    public ExtendedCurrencyCalc(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                Beneficiary beneficiary) {
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        Objects.requireNonNull(operation, "Operation can't be null");
        Objects.requireNonNull(symbol, "Symbol can't be null");
        Objects.requireNonNull(amount, "Amount can't be null");
        Objects.requireNonNull(beneficiary, "Beneficiary can't be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount );
        }

        if (delta < 0) {
            throw new IllegalArgumentException("Delta can't be less then zero, current " + delta);
        }

        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);

        if (quoteList.isEmpty()) {
            throw  new FxConversionException("There are not quotes for this symbol");
        }

        List<Quote> strikes = new ArrayList<>();
        List<Quote> withDelta = new ArrayList<>();

        for (Quote quote : quoteList) {
            if (getDeltaAmount(operation, amount, quote, 0) != null) {
                strikes.add(quote);
            } else if (getDeltaAmount(operation, amount, quote, delta) != null) {
                withDelta.add(quote);
            }
        }

        if (strikes.size() == withDelta.size() && strikes.isEmpty()) {
            return Optional.empty();
        } else {
            if (!strikes.isEmpty()) {
                return Optional.ofNullable(priceForBenicifary(strikes, beneficiary, operation));
            } else {
                return Optional.ofNullable(priceForBenicifary(withDelta, beneficiary, operation));
            }
        }
    }

    private BigDecimal priceForBenicifary(List<Quote> quotes, Beneficiary beneficiary, ClientOperation operation) {
        BigDecimal result = null;

        for (Quote quote : quotes) {
            BigDecimal price = getAmount(operation, BigDecimal.ONE, quote);
            if (result == null) {
                result =  price;
            } else {
                if (beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY
                        || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL) {
                    result = result.compareTo(price) < 0 ? price : result;
                } else if (beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL
                        || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) {

                    result = result.compareTo(price) > 0 ? price : result;
                }
            }
        }

        return result;



    }

    private BigDecimal getDeltaAmount(ClientOperation operation, BigDecimal amount, Quote quote, double delta) {
        BigDecimal midVolume = getAmount(operation, amount, quote);
        BigDecimal lowerVolume = getAmount(operation, amount.subtract(BigDecimal.valueOf(delta)), quote);
        BigDecimal upperVolume = getAmount(operation, amount.add(BigDecimal.valueOf(delta)), quote);

        if (quote.isInfinity() || midVolume.compareTo(quote.getVolumeSize()) < 0) {
            return midVolume;
        } else if (lowerVolume.compareTo(quote.getVolumeSize()) < 0) {
            return lowerVolume;
        } else if (upperVolume.compareTo(quote.getVolumeSize()) < 0) {
            return upperVolume;
        }

        return null;
    }

    private BigDecimal getAmount(ClientOperation operation, BigDecimal amount, Quote quote) {
        return operation == ClientOperation.BUY
                ? amount.divide(quote.getOffer(), 15, RoundingMode.HALF_UP)
                : amount.divide(quote.getBid(), 15, RoundingMode.HALF_UP);
    }

}
