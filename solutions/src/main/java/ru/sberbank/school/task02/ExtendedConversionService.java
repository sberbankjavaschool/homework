package ru.sberbank.school.task02;


import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExtendedConversionService extends ConversionService implements ExtendedFxConversionService {

    ExtendedConversionService(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, Beneficiary beneficiary) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return Optional.of(BigDecimal.ZERO);
        }
        List<BigDecimal> foundPrices = getPriceList(operation, symbol, amount);
        BigDecimal priceResult;
        if (foundPrices.isEmpty()) {
            return Optional.empty();
        } else {
            priceResult = getPriceBeneficiary(operation, foundPrices, beneficiary);
            return Optional.of(BigDecimal.ONE.divide(priceResult, 10, RoundingMode.HALF_DOWN));
        }
    }

    private List<BigDecimal> getPriceList(ClientOperation operation, Symbol symbol,BigDecimal amount) {
        List<Quote> quotes = getExternalQuotesService().getQuotes(symbol);
        List<BigDecimal> foundPrices = new ArrayList<>();
        BigDecimal priceQuote;
        BigDecimal amountCoverted;
        for (Quote quoteEl: quotes) {
            priceQuote = getPrice(operation, quoteEl);
            amountCoverted = amount.divide(priceQuote, 100, RoundingMode.HALF_DOWN);
            if (amountCoverted.compareTo(quoteEl.getVolumeSize()) < 0 || quoteEl.isInfinity()) {
                if (quoteEl.getVolumeSize()
                        .compareTo(getNearQuoteOfVolume(quotes, amountCoverted)
                                .getVolumeSize()) == 0) {
                    foundPrices.add(priceQuote);
                }
            }
        }
        return foundPrices;
    }

    private BigDecimal getPriceBeneficiary(ClientOperation operation,
                                           List<BigDecimal> foundPrices,
                                           Beneficiary beneficiary) {
        return (beneficiary == beneficiary.CLIENT && operation == operation.SELL ||
                beneficiary == beneficiary.BANK && operation == operation.BUY)
                ? Collections.max(foundPrices): Collections.min(foundPrices);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, double delta,
                                                Beneficiary beneficiary) {
        return Optional.empty();
    }
}
