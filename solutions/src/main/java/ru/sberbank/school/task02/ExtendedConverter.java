package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExtendedConverter extends Converter implements ExtendedFxConversionService {

    private ExternalQuotesService externalQuotesService;

    public ExtendedConverter(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                Symbol symbol,
                                                BigDecimal amount,
                                                Beneficiary beneficiary) {

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        Quote quote = null;
        Quote reserveQuote = null;

        for (Quote currQuote : quotes) {
            BigDecimal price = getPrice(operation, currQuote);
            BigDecimal usd = amount.divide(price, 15, BigDecimal.ROUND_HALF_UP);

            if (currQuote.getVolumeSize().compareTo(usd) > 0 || currQuote.isInfinity()) {
                Quote quoteUsd = searchQuote(usd, quotes);
                if (currQuote.getVolumeSize().compareTo(quoteUsd.getVolumeSize()) == 0) {
                    if (quote == null) {
                        quote = quoteUsd;
                    } else {
                        reserveQuote = quoteUsd;
                    }
                }
            }
        }

        if (quote == null) {
            return Optional.empty();
        }

        if (reserveQuote != null) {
            quote = choiceBeneficiary(quote, reserveQuote, beneficiary);
        }

        BigDecimal price = getPrice(operation, quote);

        return Optional.ofNullable(BigDecimal.ONE.divide(price, 15, BigDecimal.ROUND_HALF_UP));

    }

    private Quote choiceBeneficiary(Quote quote, Quote reserveQuote, Beneficiary beneficiary) {
        if (reserveQuote.isInfinity()) {
            return beneficiary == Beneficiary.CLIENT ? reserveQuote : quote;
        }

        if (quote.isInfinity()) {
            return beneficiary == Beneficiary.CLIENT ? quote : reserveQuote;
        }

        if (quote.getVolumeSize().compareTo(reserveQuote.getVolumeSize()) < 0) {
            return beneficiary == Beneficiary.CLIENT ? reserveQuote : quote;
        }

        return quote;
    }
}
