package ru.sberbank.school.task02;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

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
        this.exQuotes = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null) {
            throw new NullPointerException("Operation is null");
        }
        if (symbol == null) {
            throw new NullPointerException("Symbol is null");
        }
        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Amount is equal to ZERO");
        }

        List<Quote> quotes = exQuotes.getQuotes(symbol);
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
        return (operation == ClientOperation.BUY ? exRate.getOffer() : exRate.getBid());

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
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                         Symbol symbol,
                                         BigDecimal amount,
                                         Beneficiary beneficiary) {

        ClientOperation revertedOperation = operation == ClientOperation.BUY ? ClientOperation.SELL
                : ClientOperation.BUY;
        BigDecimal rightCur = convert(revertedOperation, symbol, amount);

        BigDecimal rightAmount = amount.divide(rightCur, 10, RoundingMode.HALF_UP);

        BigDecimal revCur = convert(revertedOperation, symbol, rightAmount);

        BigDecimal price = BigDecimal.ONE.divide(revCur, 10, RoundingMode.HALF_UP);
        return Optional.ofNullable(price);

    }

}

