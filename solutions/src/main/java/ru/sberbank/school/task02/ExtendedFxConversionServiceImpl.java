package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ExtendedFxConversionServiceImpl extends FxConversionServiceImpl implements ExtendedFxConversionService {

    public ExtendedFxConversionServiceImpl(@NonNull ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Объем не может быть отрицательным или равным нулю");
        }
        List<Quote> quoteList = externalQuotesService.getQuotes(symbol);
        if (quoteList == null || quoteList.isEmpty()) {
            return Optional.empty();
        }

        quoteList.sort((o1, o2) -> {
            if (o1.isInfinity()) {
                return 1;
            } else if (o2.isInfinity()) {
                return -1;
            } else {
                return o1.getVolumeSize().subtract(o2.getVolumeSize()).compareTo(BigDecimal.ZERO);
            }
        });

        Quote targetQuote;
        if ((operation == ClientOperation.BUY && beneficiary == Beneficiary.BANK)
                || (operation == ClientOperation.SELL && beneficiary == Beneficiary.CLIENT)) {
            targetQuote = getFirstConcurrency(quoteList, amount);
        } else {
            targetQuote = getLastConcurrency(quoteList, amount);
        }

        if (targetQuote == null) {
            return Optional.empty();
        }

        BigDecimal price = BigDecimal.ONE.divide(
                operation == ClientOperation.BUY ? targetQuote.getOffer() : targetQuote.getBid(),
                10, BigDecimal.ROUND_HALF_UP);


        return Optional.of(price);
    }

    /**
     * Возвращает значение цены единицы котируемой валюты для указанного объема.
     *
     * @param operation   вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param delta       допустимое отклонение при отсутствии точного попадания в котируемой валюте
     * @param beneficiary В чью пользу осуществляется округление
     * @return Цена для указанного объема
     */
    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                Symbol symbol,
                                                BigDecimal amount,
                                                double delta,
                                                Beneficiary beneficiary) {
        return Optional.empty();
    }

    private Quote getLastConcurrency(List<Quote> quoteList, BigDecimal amount) {
        Quote currentQuote = null;
        Quote quote;
        for (int i = quoteList.size() - 1; i >= 0; i--) {
            quote = quoteList.get(i);
            if (quote.getVolumeSize().compareTo(amount) > 0) {
                currentQuote = quote;
                break;
            }
        }
        return currentQuote;
    }

    private Quote getFirstConcurrency(List<Quote> quoteList, BigDecimal amount) {
        Quote currentQuote = null;
        for (Quote quote : quoteList) {
            if (!quote.isInfinity() && quote.getVolumeSize().compareTo(amount) > 0) {
                currentQuote = quote;
                break;
            }
        }
        return currentQuote;
    }
}