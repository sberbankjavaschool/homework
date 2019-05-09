package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
        Quote current = null;
        for (Quote quote: quotes) {
            BigDecimal price = operation == ClientOperation.BUY ? quote.getBid() : quote.getOffer();
            BigDecimal reverseAmount = amount.divide(price, 10, RoundingMode.HALF_UP);
            current = calculator.checkQuote(reverseAmount, quote, current);
        }
        if (current == null) {
            return Optional.empty();
        }
        BigDecimal priceResult = operation == ClientOperation.BUY ? current.getBid() : current.getOffer();
        BigDecimal revertResult = BigDecimal.ONE.divide(priceResult, 10, RoundingMode.HALF_UP);
        return Optional.ofNullable(revertResult);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        if (delta == 0) {
            return convertReversed(operation, symbol, amount, beneficiary);
        } else {
            Optional<BigDecimal> revertResultHighDelta = convertReversed(operation, symbol,
                    amount.add(BigDecimal.valueOf(delta)), beneficiary);
            Optional<BigDecimal> revertResultLowDelta = convertReversed(operation, symbol,
                    amount.subtract(BigDecimal.valueOf(delta)), beneficiary);
            if (!revertResultHighDelta.isPresent()) {
                return revertResultLowDelta;
            }
            if (!revertResultLowDelta.isPresent()) {
                return revertResultHighDelta;
            }
            if (revertResultHighDelta.get().compareTo(revertResultLowDelta.get()) > 0 ) {
                if (operation == ClientOperation.BUY && beneficiary == Beneficiary.BANK
                        || operation == ClientOperation.SELL && beneficiary == Beneficiary.CLIENT) {
                    return revertResultHighDelta;
                } else {
                    return revertResultLowDelta;
                }
            } else {
                if (operation == ClientOperation.BUY && beneficiary == Beneficiary.BANK
                        || operation == ClientOperation.SELL && beneficiary == Beneficiary.CLIENT) {
                    return revertResultLowDelta;
                } else {
                    return revertResultHighDelta;
                }
            }
        }
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        return calculator.convert(operation, symbol, amount);
    }

    private void validate(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        calculator.validate(operation, symbol, amount);
        if (beneficiary == null) {
            throw new NullPointerException("No beneficiary provided");
        }
    }
}
