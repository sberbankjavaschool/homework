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
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        if (operation == null || symbol == null || amount == null) {
            throw new NullPointerException("Arguments can't be null:\n" + "Operation: " + operation + "\n"
                    + "Symbol: " + symbol + "\n" + "Amount: " + amount + "\n" + "Beneficiary: " + beneficiary);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || delta < 0) {
            throw new IllegalArgumentException("Amount can't be zero or less, current " + amount + "\n"
                    + "delta can't be less that zero, current " + delta);
        }

        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);

        if (quoteList.isEmpty()) {
            throw  new IllegalArgumentException("There are not quotes for this symbol");
        }

        BigDecimal bigDecimalDelta = BigDecimal.valueOf(delta);
        Quote result = null;

        for (Quote quote : quoteList) {
            BigDecimal localAmount = operation == ClientOperation.BUY
                    ? amount.divide(quote.getOffer(), RoundingMode.HALF_UP)
                    : amount.divide(quote.getBid(), RoundingMode.HALF_UP);

            if (!quote.isInfinity()
                    && quote.getVolumeSize().subtract(localAmount).abs().compareTo(bigDecimalDelta) <= 0) {
                if (result == null) {
                    result = quote;
                } else {
                    result = getQuotePleasureForBeneficiary(result, quote, beneficiary);
                }
            }
        }

        if (result == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(operation == ClientOperation.BUY ? result.getOffer() : result.getBid());

    }

    private Quote getQuotePleasureForBeneficiary(Quote quote1, Quote quote2, Beneficiary beneficiary) {
        if (beneficiary == Beneficiary.BANK) {
            return quote1.getOffer().compareTo(quote2.getOffer()) > 0 ? quote1 : quote2;
        }

        if (beneficiary == Beneficiary.CLIENT) {
            return quote1.getOffer().compareTo(quote2.getOffer()) > 0 ? quote2 : quote1;
        }

        return quote1;
    }

}
