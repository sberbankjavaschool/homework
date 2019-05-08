package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
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
                                                Beneficiary beneficiary) throws ConverterConfigurationException {
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConverterConfigurationException("Wrong amount provided");
        }
        List<Quote> quotes = provider.getQuotes(symbol);
        if (quotes.isEmpty()) {
            return Optional.empty();
        }

        Quote current = null;
        for (Quote quote: quotes) {
            BigDecimal reverseAmount = operation == ClientOperation.BUY
                    ? amount.divide(quote.getBid(), RoundingMode.HALF_UP)
                    : amount.divide(quote.getOffer(), RoundingMode.HALF_UP);
            if (delta == 0) {
                current = calculator.checkQuote(reverseAmount, quote, current);
            } else {
                Quote currentUp = calculator.checkQuote(
                        reverseAmount.add(BigDecimal.valueOf(delta)), quote, current);
                Quote currentDown = calculator.checkQuote(
                        reverseAmount.subtract(BigDecimal.valueOf(delta)), quote, current);
                if (currentUp == null) {
                    current = currentDown;
                } else if (currentDown == null || currentUp.equals(currentDown)) {
                    current = currentUp;
                } else {
                    if (beneficiary == Beneficiary.BANK) {
                        current = currentUp.getBid().compareTo(currentDown.getBid()) > 0 ? currentUp : currentDown;
                    } else {
                        current = currentUp.getBid().compareTo(currentDown.getBid()) <= 0 ? currentUp : currentDown;
                    }
                }
            }
        }
        if (current == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(operation == ClientOperation.BUY ? current.getBid() : current.getOffer());
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        return calculator.convert(operation, symbol, amount);
    }
}
