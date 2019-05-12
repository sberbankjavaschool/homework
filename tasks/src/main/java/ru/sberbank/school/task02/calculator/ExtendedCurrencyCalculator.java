package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import java.math.BigDecimal;
import java.util.Optional;

public class ExtendedCurrencyCalculator extends CurrencyCalculator implements ExtendedFxConversionService {
    private int rounding_mode;

    public ExtendedCurrencyCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    private Optional<BigDecimal> extendedOperation(ClientOperation operation, Beneficiary beneficiary) {
        Optional<Quote> quote = findQuote(new CompareQuotesBenificiary(operation, beneficiary));
        if (!quote.isPresent()) {
            System.out.println("Return Optional.empty()");
            return Optional.empty();
        }
        showQuote(quote.get());
        if (operation == ClientOperation.SELL) {
                return Optional.of(BigDecimal.valueOf(1).divide(quote.get().getBid(),10, rounding_mode));
        }
        if (operation == ClientOperation.BUY) {
            return Optional.of(BigDecimal.valueOf(1).divide(quote.get().getOffer(), 10, rounding_mode));
        }
        System.out.println("Return Optional.empty()");
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        quotes = externalQuotesService.getQuotes(Symbol.USD_RUB);
        this.symbolOfRequest = symbol;
        this.amountOfRequest = amount;
        System.out.println("Get benificiary: " + beneficiary.toString());
        System.out.println("Get symbol: " + symbol.getSymbol());
        System.out.println("Get client operation: " + operation.toString());
        System.out.println("Get request volume: " +  amountOfRequest );
        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY)
                || (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL)) {
            rounding_mode = BigDecimal.ROUND_CEILING;
        }
        if ((beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL)
                || (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY)) {
            rounding_mode = BigDecimal.ROUND_FLOOR;
        }
        this.amountOfRequest = amount.setScale(10,  rounding_mode);

        if (check()) {
            return extendedOperation(operation, beneficiary);
        }
        System.out.println("Return Optional.empty() after check");
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                Symbol symbol,
                                                BigDecimal amount,
                                                double delta,
                                                Beneficiary beneficiary) {
         return Optional.empty();
    }
}
