package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReverseCalculator implements ExtendedFxConversionService {

    private Calculator calculator;
    private ExternalQuotesService provider;


    ReverseCalculator(ExternalQuotesService provider) {
        this.provider = provider;
        this.calculator = new Calculator(provider);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                Beneficiary beneficiary) {
        validate(operation, symbol, amount, beneficiary);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Wrong amount");
        }
        List<Quote> quotes = provider.getQuotes(symbol);
        if (quotes.isEmpty()) {
            throw new FxConversionException("No quotes found");
        }

        List<Quote> filteredQuotes = filterQuotes(amount, quotes, operation);

        if (filteredQuotes.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal priceResult;
        if (filteredQuotes.size() == 1) {
            Quote current = filteredQuotes.get(0);
            priceResult = operation == ClientOperation.BUY ? current.getBid() : current.getOffer();
         } else {
            if (operation == ClientOperation.BUY && beneficiary == Beneficiary.CLIENT) {
                priceResult = Collections.min(filteredQuotes, Comparator.comparing(Quote::getBid)).getBid();
            } else if (operation == ClientOperation.BUY && beneficiary == Beneficiary.BANK) {
                priceResult = Collections.max(filteredQuotes, Comparator.comparing(Quote::getBid)).getBid();
            } else if (operation == ClientOperation.SELL && beneficiary == Beneficiary.CLIENT) {
                priceResult = Collections.max(filteredQuotes, Comparator.comparing(Quote::getOffer)).getOffer();
            } else {
                priceResult = Collections.min(filteredQuotes, Comparator.comparing(Quote::getOffer)).getOffer();
            }
        }
        return Optional.ofNullable(BigDecimal.ONE.divide(priceResult, 10, RoundingMode.HALF_UP));
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {

        if (delta < 0) {
            throw new IllegalArgumentException("Negative delta is mindless");
        }
        if (delta == 0) {
            return convertReversed(operation, symbol, amount, beneficiary);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        return calculator.convert(operation, symbol, amount);
    }

    private List<Quote> filterQuotes(BigDecimal amount, List<Quote> quotes, ClientOperation operation) {
        quotes.sort((left, right) -> {
            if (left.isInfinity()) {
                return 1;
            }
            if (right.isInfinity()) {
                return -1;
            }
            return left.getVolumeSize().compareTo(right.getVolumeSize());
        });

        List<Quote> filteredQuotes = new ArrayList<>();
        BigDecimal lastVolume = BigDecimal.ZERO;
        for (Quote quote : quotes) {
            BigDecimal price = operation == ClientOperation.BUY ? quote.getBid() : quote.getOffer();
            if (amount.compareTo(lastVolume.multiply(price)) >= 0
                    && amount.compareTo(quote.getVolumeSize().multiply(price)) < 0) {
                filteredQuotes.add(quote);
            }
        }
        return filteredQuotes;
    }

    private void validate(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        calculator.validate(operation, symbol, amount);
        Objects.requireNonNull(beneficiary, "No beneficiary provided");
    }

}