package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExConverter extends Converter implements ExtendedFxConversionService {
    private ExternalQuotesService service;

    public ExConverter(ExternalQuotesService service) {
        super(service);
        this.service = service;
    }

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation,
                                                @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount,
                                                         double delta,
                                                @NonNull Beneficiary beneficiary) {
        return convertReversed(operation,symbol,amount,beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation,
                                                @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount,
                                                @NonNull Beneficiary beneficiary) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount can't be lower or equals 0");
        }

        List<Quote> quotes = service.getQuotes(symbol);
        if (quotes.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal tempCount;
        Quote resultQuote = null;
        Quote lastQuote = null;

        for (Quote q : quotes) {
            tempCount = getCount(q,amount,operation);

            if (quoteIsMatch(q, tempCount) && (lastQuote == null || compareQuotes(lastQuote,q,operation,amount))) {
                resultQuote = chooseBenef(lastQuote, q, operation,beneficiary);
                lastQuote = q;
            }
        }

        if (resultQuote == null) {
            return Optional.empty();
        }

        return Optional.of(BigDecimal.ONE.divide(getPrice(resultQuote,operation),
                                            10,
                                                RoundingMode.HALF_UP));

    }

    private boolean quoteIsMatch(Quote quote, BigDecimal amount) {
        return ((quote.getVolumeSize().compareTo(amount) > 0) && !quote.isInfinity());
    }

    private BigDecimal getPrice(Quote quote, ClientOperation operation) {
        return operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid();
    }


    private BigDecimal getCount(Quote quote, BigDecimal amount, ClientOperation operation) {
        return operation == ClientOperation.BUY
                ? amount.divide(quote.getOffer(),
                10,
                RoundingMode.HALF_UP)
                : amount.divide(quote.getBid(),
                10,
                RoundingMode.HALF_UP);
    }

    private boolean compareQuotes(Quote lastQuote, Quote candidateQuote, ClientOperation operation, BigDecimal amount) {
        return getCount(candidateQuote, amount, operation).compareTo(lastQuote.getVolumeSize()) > 0;
    }

    private Quote chooseBenef(Quote q1, Quote q2, ClientOperation operation, Beneficiary beneficiary) {
        if (q1 == null) {
            return q2;
        }
        if (q2 == null) {
            return q1;
        }
        if (beneficiary == Beneficiary.CLIENT) {
            if (operation == ClientOperation.BUY) {
                return q1.getOffer().compareTo(q2.getOffer()) < 0 ? q1 : q2;
            } else {
                return q1.getBid().compareTo(q2.getBid()) < 0 ? q2 : q1;
            }
        } else {
            if (operation == ClientOperation.BUY) {
                return q1.getOffer().compareTo(q2.getOffer()) < 0 ? q2 : q1;
            } else {
                return q1.getBid().compareTo(q2.getBid()) < 0 ? q1 : q2;
            }
        }
    }
}
