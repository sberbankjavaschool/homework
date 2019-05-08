package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
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
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        checkData(quotes, amount, beneficiary);

        Quote quote = null;
        Quote reserveQuote = null;

        for (Quote currQuote : quotes) {
            BigDecimal price = getPrice(operation, currQuote);
            BigDecimal usd = amount.divide(price, 10, BigDecimal.ROUND_HALF_UP);

            if (currQuote.getVolumeSize().compareTo(usd) > 0 || currQuote.isInfinity()) {
                Quote quote3 = searchQuote(usd, quotes);
                if (currQuote == quote3) {
                    if (quote == null)
                        quote = quote3;
                    else
                        reserveQuote = quote3;
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

        return Optional.ofNullable(BigDecimal.ONE.divide(price, 10, BigDecimal.ROUND_HALF_UP));

    }


    private void checkData(List<Quote> quotes, BigDecimal amount, Beneficiary beneficiary) {
        super.checkData(quotes, amount);
        if (beneficiary != Beneficiary.CLIENT && beneficiary != Beneficiary.BANK) {
            throw new ConverterConfigurationException("Incorrect beneficiary");
        }
    }


    private Quote choiceBeneficiary(Quote quote, Quote reserveQuote, Beneficiary beneficiary) {
        if (beneficiary == Beneficiary.CLIENT) {
            if (reserveQuote.isInfinity()) return reserveQuote;
            if (quote.isInfinity()) return quote;
            if (quote.getVolumeSize().compareTo(reserveQuote.getVolumeSize()) < 0) return reserveQuote;

        } else {
            if (reserveQuote.isInfinity()) return quote;
            if (quote.isInfinity()) return reserveQuote;
            if (quote.getVolumeSize().compareTo(reserveQuote.getVolumeSize()) > 0) return reserveQuote;
        }
        return quote;
    }

}
