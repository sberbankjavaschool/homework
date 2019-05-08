package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConverterConfigurationException("Wrong amount provided");
        }
        List<Quote> quotes = provider.getQuotes(symbol);
        if (quotes.isEmpty()) {
            throw new WrongSymbolException("No quotes for this symbol");
        }
        Quote current = null;
        for (Quote quote: quotes) {
            BigDecimal reverseAmount = operation == ClientOperation.BUY
                    ? amount.divide(quote.getBid(), 10, RoundingMode.HALF_UP)
                    : amount.divide(quote.getOffer(), 10, RoundingMode.HALF_UP);
            current = calculator.checkQuote(reverseAmount, quote, current);
        }
        if (current == null) {
            return Optional.empty();
        }
        BigDecimal revertResult = operation == ClientOperation.BUY
                ? BigDecimal.ONE.divide(current.getBid(), 10, RoundingMode.HALF_UP)
                : BigDecimal.ONE.divide(current.getOffer(), 10, RoundingMode.HALF_UP);
        return Optional.ofNullable(revertResult);
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        return calculator.convert(operation, symbol, amount);
    }
}
