package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        List<Quote> quotesResult = new ArrayList<>();

        for (Quote currQuote : quotes) {
            BigDecimal price = getPrice(operation, currQuote);
            BigDecimal amountBaseCurrency = amount.divide(price, 10, BigDecimal.ROUND_HALF_UP);

            if (currQuote.getVolumeSize().compareTo(amountBaseCurrency) > 0 || currQuote.isInfinity()) {
                Quote quoteBaseCurrency = searchQuote(amountBaseCurrency, quotes);
                if (currQuote.getVolumeSize().compareTo(quoteBaseCurrency.getVolumeSize()) == 0) {
                    quotesResult.add(currQuote);
                }
            }
        }

        if (quotesResult.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal price = choicePriceByBeneficiary(quotesResult, beneficiary, operation);

        return Optional.ofNullable(BigDecimal.ONE.divide(price, 10, BigDecimal.ROUND_HALF_UP));

    }

    private BigDecimal choicePriceByBeneficiary(List<Quote> quotes,
                                                Beneficiary beneficiary,
                                                ClientOperation operation) {
        BigDecimal priceResult = getPrice(operation, quotes.get(0));

        if (quotes.size() == 1) {
            return priceResult;
        }

        for (Quote quote : quotes) {
            BigDecimal price = getPrice(operation, quote);

            if (price.compareTo(priceResult) > 0) {
                if (beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY
                        || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL) {
                    priceResult = price;
                }
            } else {
                if (beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL
                        || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) {
                    priceResult = price;
                }
            }
        }
        return priceResult;
    }
}
