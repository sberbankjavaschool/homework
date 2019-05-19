package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import lombok.NonNull;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.exception.FxConversionException;


public class FxConversionServiceImpl implements ExtendedFxConversionService {

    /**
     * Возвращает значение цены единицы базовой валюты для указанного объема.
     *
     * @param operation вид операции
     * @param symbol    Инструмент
     * @param amount    Объем
     * @return Цена для указанного объема
     */
    private ExternalQuotesService exQuotes;

    public FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        if (externalQuotesService == null) {
            throw new IllegalArgumentException("List of quotes is empty");
        }
        this.exQuotes = externalQuotesService;
    }

    @Override
    public BigDecimal convert(@NonNull ClientOperation operation,
                              @NonNull Symbol symbol,
                              @NonNull BigDecimal amount) {
        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Amount is equal to ZERO");
        }

        List<Quote> quotes = exQuotes.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("No quotes found");
        }
        Quote exRate = findRightQuote(quotes, amount);
        if (exRate == null || exRate.getOffer() == null || exRate.getBid() == null) {
            throw new FxConversionException("no rates for this amount");
        }
        return (operation == ClientOperation.BUY ? exRate.getOffer() : exRate.getBid());

    }

    protected Quote findRightQuote(List<Quote> quotes, BigDecimal amount) {
        Quote exRate = null;

        for (Quote q : quotes) {
            if (exRate == null && q.isInfinity()) {
                exRate = q;
            } else if (amount.compareTo(q.getVolumeSize()) < 0) {
                if (exRate == null || exRate.isInfinity() || q.getVolumeSize().compareTo(exRate.getVolumeSize()) < 0) {
                    exRate = q;
                }
            }
        }
        return exRate;
    }


    /**
     * Возвращает значение цены единицы котируемой валюты для указанного объема
     * только в том случае если есть полное совпадение.
     *
     * @param operation   вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param beneficiary В чью пользу осуществляется округление
     * @return Цена для указанного объема
     */

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation,
                                                @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount,
                                                @NonNull Beneficiary beneficiary) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Wrong amount");
        }
        List<Quote> rightQuotes = createRightQuotes(exQuotes.getQuotes(symbol), amount, symbol, operation);
        if (rightQuotes.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal revCur = operation == ClientOperation.BUY ?
                rightQuotes.iterator().next().getOffer() : rightQuotes.iterator().next().getBid();
        if (rightQuotes.size() > 1) {
            revCur = BeneficiaryDepend(beneficiary, rightQuotes, revCur, operation);
        }
        return Optional.ofNullable(BigDecimal.ONE.divide(revCur,10,RoundingMode.HALF_UP));
    }

    protected BigDecimal BeneficiaryDepend(Beneficiary beneficiary, List<Quote> quotes,
                                           BigDecimal price, ClientOperation operation) {
        for (Quote q : quotes) {
            BigDecimal curPrice = operation == ClientOperation.BUY ? q.getOffer() : q.getBid();

            if (curPrice.compareTo(price) > 0) {
                if (beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY
                        || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL) {
                    price = curPrice;
                }
            } else {
                if (beneficiary == Beneficiary.BANK && operation == ClientOperation.SELL
                        || beneficiary == Beneficiary.CLIENT && operation == ClientOperation.BUY) {
                    price = curPrice;
                }
            }
        }

        return price;
    }

    protected List<Quote> createRightQuotes(List<Quote> quotes, BigDecimal amount,
                                            Symbol symbol, ClientOperation operation) {
        List<Quote> rightQuotes = new ArrayList<>();
        for (Quote q : quotes) {
            BigDecimal curCur = operation == ClientOperation.BUY ? q.getOffer() : q.getBid();
            BigDecimal curAmount = amount.divide(curCur, 10, RoundingMode.HALF_UP);
            if ( q.getVolumeSize().compareTo(curAmount) > 0) {
                Quote rightQuote = findRightQuote(quotes, curAmount);
                if (q.getVolumeSize().compareTo(rightQuote.getVolumeSize()) == 0) {
                    rightQuotes.add(q);
                }
            }
        }
        return rightQuotes;
    }

}

