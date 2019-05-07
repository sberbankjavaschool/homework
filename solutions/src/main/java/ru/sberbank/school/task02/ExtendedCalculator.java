package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

/**
 * Реализация обратного валютного калькулятора.
 */
public class ExtendedCalculator extends Calculator implements ExtendedFxConversionService {

    public ExtendedCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                Beneficiary beneficiary) {

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new EmptyQuoteException("No quotes!");
        }

        List<BigDecimal> prices = new ArrayList<>();
        BigDecimal volume;

        for (Quote q : quotes) {
            volume = operation == ClientOperation.SELL ? amount.divide(q.getBid(), RoundingMode.HALF_UP) : amount.divide(q.getOffer(), RoundingMode.HALF_UP);
            if (volume.compareTo(q.getVolumeSize()) < 0) {
                prices.add(operation == ClientOperation.SELL ? q.getBid() : q.getOffer());
            }
        }

        if (prices.isEmpty()) {
            return Optional.empty();
        }

        Collections.sort(prices);
        return Optional.of(beneficiary == Beneficiary.CLIENT ? prices.get(0) : prices.get(prices.size() - 1));
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {
        return Optional.empty();
    }
}

