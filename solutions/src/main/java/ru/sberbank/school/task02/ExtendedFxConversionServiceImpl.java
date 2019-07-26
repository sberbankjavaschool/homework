package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Mart
 * 10.05.2019
 **/
public class ExtendedFxConversionServiceImpl extends FxConversionServiceImpl implements ExtendedFxConversionService {

    public ExtendedFxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                Symbol symbol,
                                                BigDecimal amount,
                                                Beneficiary beneficiary) {

        return convertReversed(operation, symbol, amount,0, beneficiary);
    }

    /**
     *
     * @param operation   вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param delta       допустимое отклонение при отсутствии точного попадания в котируемой валюте
     * @param beneficiary В чью пользу осуществляется округление
     * @return
     */
    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation,
                                                @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount,
                                                double delta,
                                                @NonNull Beneficiary beneficiary) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Объем операции должен быть больше 0");
        }

        List<Quote> quotes = getQuotesService().getQuotes(symbol);
        Set<Quote> exactMatch = new HashSet<>();

        setQuotes(quotes, exactMatch, operation, amount);

        if (exactMatch.isEmpty()) {
            return  Optional.empty();
        }

        BigDecimal price = getByBeneficiary(exactMatch, beneficiary, operation);

        return Optional.ofNullable(BigDecimal.valueOf(1).divide(price, 10, RoundingMode.HALF_UP));
    }

    private void setQuotes(List<Quote> quotes,
                           Set<Quote> exactMatch,
                           ClientOperation operation,
                           BigDecimal amount) {

        for (Quote quote : quotes) {
            BigDecimal exactFitVolume = getExactAmount(operation, quote, amount);

            if (exactFitVolume.compareTo(quote.getVolumeSize()) < 0 || quote.isInfinity()) {
                Quote tmp = findQuote(exactFitVolume, quotes);
                if (quote.getVolumeSize().compareTo(tmp.getVolumeSize()) == 0) {
                    exactMatch.add(quote);
                }
            }
        }
    }

    private BigDecimal getByBeneficiary(Set<Quote> quotes,Beneficiary beneficiary, ClientOperation operation) {
        Quote quoteFirst = quotes.iterator().next();
        BigDecimal result = getPrice(operation, quoteFirst);

        boolean lessGood = isLessGood(beneficiary, operation);
        boolean moreGood = isMoreGood(beneficiary, operation);

        for (Quote quote : quotes) {
            BigDecimal currentPrice = getPrice(operation, quote);

            if (currentPrice.compareTo(result) < 0) {
                if (lessGood) {
                    result = currentPrice;
                }
            } else {
                if (moreGood) {
                    result = currentPrice;
                }
            }
        }
        return result;
    }

    private BigDecimal getPrice(ClientOperation operation, Quote quote) {
        return operation == ClientOperation.BUY
                ? quote.getOffer() : quote.getBid();
    }

    private boolean isLessGood(Beneficiary beneficiary, ClientOperation operation) {
        return beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL
                || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY;
    }

    private boolean isMoreGood(Beneficiary beneficiary, ClientOperation operation) {
        return beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY
                || beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL;
    }

    private BigDecimal getExactAmount(ClientOperation operation, Quote quote, BigDecimal amount) {
        return  operation == ClientOperation.BUY
                ? amount.divide(quote.getOffer(), 10, RoundingMode.HALF_UP)
                : amount.divide(quote.getBid(), 10, RoundingMode.HALF_UP);
    }
}
