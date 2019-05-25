package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

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

        Map<String, List<Quote>> suitQoutes = getSuitableQuotes(quoteList, operation, amount, delta);

        List<Quote> strikes = suitQoutes.get("strikes");
        List<Quote> withDelta = suitQoutes.get("withDelta");

        if (strikes.isEmpty() && withDelta.isEmpty()) {
            return Optional.empty();
        } else if (!strikes.isEmpty()) {
            return Optional.ofNullable(priceForBenicifary(strikes, beneficiary, operation));
        } else {
            return Optional.ofNullable(priceForBenicifary(withDelta, beneficiary, operation));
        }
    }

    private Map<String, List<Quote>> getSuitableQuotes(List<Quote> quotes, ClientOperation operation,
                                                       BigDecimal amount, double delta) {
        List<Quote> strikes = new ArrayList<>();
        List<Quote> withDelta = new ArrayList<>();

        List<ReversQuotesBorders> bordersList = getListOfBorders(quotes, operation);

        BigDecimal subAmount = amount.subtract(BigDecimal.valueOf(delta));
        BigDecimal addAmount = amount.add(BigDecimal.valueOf(delta));

        for (ReversQuotesBorders borders : bordersList) {
            if (borders.getUpperBorder() == null) {
                if (borders.getLowerBorder().compareTo(amount) <= 0) {
                    strikes.add(borders.getQuote());
                } else if (borders.getLowerBorder().compareTo(subAmount) <= 0
                        || borders.getLowerBorder().compareTo(addAmount) < 0) {
                    withDelta.add(borders.getQuote());
                }
            } else {
                if (borders.getLowerBorder().compareTo(amount) <= 0
                        && borders.getUpperBorder().compareTo(amount) > 0) {
                    strikes.add(borders.getQuote());
                } else if ((borders.getLowerBorder().compareTo(subAmount) <= 0
                        && borders.getUpperBorder().compareTo(subAmount) > 0)
                        || (borders.getLowerBorder().compareTo(addAmount) <= 0
                        && borders.getUpperBorder().compareTo(addAmount) > 0)) {
                    withDelta.add(borders.getQuote());
                }
            }
        }

        Map<String, List<Quote>> result = new HashMap<>();

        result.put("strikes", strikes);
        result.put("withDelta", withDelta);

        return result;
    }

    private List<ReversQuotesBorders> getListOfBorders(List<Quote> quotes, ClientOperation operation) {
        quotes.sort((quote1, quote2) -> {
            if (quote1.isInfinity()) {
                return 1;
            } else if (quote2.isInfinity()) {
                return -1;
            } else {
                return quote1.getVolumeSize().compareTo(quote2.getVolumeSize());
            }
        });

        List<ReversQuotesBorders> result = new ArrayList<>();
        Quote prefer = null;

        for (Quote quote : quotes) {
            result.add(new ReversQuotesBorders(prefer, quote, operation));
            prefer = quote;
        }

        return result;
    }

    private BigDecimal priceForBenicifary(List<Quote> quotes, Beneficiary beneficiary, ClientOperation operation) {
        BigDecimal result = null;

        for (Quote quote : quotes) {
            BigDecimal price = calculatePrice(operation, BigDecimal.ONE, quote);
            if (result == null) {
                result =  price;
            }
            if (beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY
                    || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL) {
                result = result.compareTo(price) < 0 ? price : result;
            } else if (beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL
                    || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) {

                result = result.compareTo(price) > 0 ? price : result;
            }
        }
        
        return result;
    }

    private BigDecimal calculatePrice(ClientOperation operation, BigDecimal amount, Quote quote) {
        return operation == ClientOperation.BUY
                ? amount.divide(quote.getOffer(), 10, RoundingMode.HALF_UP)
                : amount.divide(quote.getBid(), 10, RoundingMode.HALF_UP);
    }
}
