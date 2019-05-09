package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class ExtendedCurrencyCalc extends CurrencyCalculator implements ExtendedFxConversionService {
    private ExternalQuotesService externalQuotesService;

    public ExtendedCurrencyCalc(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                Beneficiary beneficiary) {
        if (operation == null || symbol == null || amount == null || beneficiary == null) {
            throw new NullPointerException("Arguments can't be null:\n" + "Operation: " + operation + "\n"
                    + "Symbol: " + symbol + "\n" + "Amount: " + amount + "\n" + "Beneficiary: " + beneficiary);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount );
        }

        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);

        if (quoteList.isEmpty()) {
            throw  new IllegalArgumentException("There are not quotes for this symbol");
        }

        Quote result = null;

        for (Quote quote : quoteList) {
            result = chooseQuote(getAmount(operation, amount, quote), result, quote);
        }

        if (result == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(getAmount(operation, BigDecimal.ONE, result));
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        return Optional.empty();

    }

    private BigDecimal getAmount(ClientOperation operation, BigDecimal amount, Quote quote) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount);
        }

        return operation == ClientOperation.BUY
                ? amount.divide(quote.getOffer(), 10, RoundingMode.HALF_UP)
                : amount.divide(quote.getBid(), 10, RoundingMode.HALF_UP);
    }

}
