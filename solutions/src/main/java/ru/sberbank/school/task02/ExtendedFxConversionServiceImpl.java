package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        List<Quote> suitableQuoteList = getSuitableQuoteList(quoteList, amount, operation);
        Quote targetQuote;
        if (suitableQuoteList.size() == 0) {
            return Optional.empty();
        } else if (suitableQuoteList.size() == 1) {
            targetQuote = suitableQuoteList.get(0);
        } else {
            targetQuote = getSuitableQuoteWithBeneficiary(suitableQuoteList, operation, beneficiary);
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

    private Quote getSuitableQuoteWithBeneficiary(List<Quote> suitableQuoteList,
                                                  ClientOperation operation,
                                                  Beneficiary beneficiary) {
        Quote targetQuote = suitableQuoteList.get(0);
        if (suitableQuoteList.size() == 1) {
            return targetQuote;
        }
        sortQuoteListByPrice(suitableQuoteList, operation);
        if ((operation == ClientOperation.SELL && beneficiary == Beneficiary.BANK)
                || (operation == ClientOperation.BUY && beneficiary == Beneficiary.CLIENT)) {
            targetQuote = suitableQuoteList.get(0);
        } else {
            targetQuote = suitableQuoteList.get(suitableQuoteList.size() - 1);
        }
        return targetQuote;
    }

    private void sortQuoteListByPrice(List<Quote> suitableQuoteList, ClientOperation operation) {
        if (operation == ClientOperation.BUY) {
            suitableQuoteList.sort(Comparator.comparing(Quote::getOffer));
        } else {
            suitableQuoteList.sort(Comparator.comparing(Quote::getBid));
        }
    }

    private List<Quote> getSuitableQuoteList(List<Quote> quoteList,
                                             BigDecimal amount,
                                             ClientOperation operation) {
        List<Quote> suitableQuoteList = new ArrayList<>();

        if (quoteList.size() == 1 && quoteList.get(0).isInfinity()) {
            return quoteList;
        }
        for (Quote curQuote : quoteList) {
            BigDecimal curPrice = operation == ClientOperation.BUY ? curQuote.getOffer() : curQuote.getBid();
            BigDecimal curAmount = amount.divide(curPrice, 10, BigDecimal.ROUND_HALF_UP);
            if (curQuote.equals(findQuote(quoteList, curAmount))) {
                suitableQuoteList.add(curQuote);
            }
        }
        return suitableQuoteList;
    }
}