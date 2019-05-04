package ru.sberbank.school.task02.services.implementation;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.services.InternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ru.sberbank.school.task02.util.Beneficiary.BANK;
import static ru.sberbank.school.task02.util.Beneficiary.CLIENT;
import static ru.sberbank.school.task02.util.ClientOperation.BUY;
import static ru.sberbank.school.task02.util.ClientOperation.SELL;

/**
 * Обратный валютный кальклятор: возвращает значение цены единицы котируемой валюты для заданного объема базовой.
 * Для параметров SELL, USD/RUB, 1000, CLIENT, ситуация читается так:
 * Клиент хочет продать долларов на 1000 рублей, сказать сколько будет стоить 1 доллар.
 * Created by Gregory Melnikov at 01.05.2019
 */
public class ExtendedFxConversionServiceImpl extends FxConversionServiceImpl implements ExtendedFxConversionService {
    
    ExtendedFxConversionServiceImpl(InternalQuotesService internalQuotesService) {
        super(internalQuotesService);
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

        List<Quote> quotes = internalQuotesService.getQuotes(symbol);

        Quote targetQuote = findQuote(quotes, operation, amount, beneficiary);

        BigDecimal quoteRate = operation == BUY ? targetQuote.getOffer() : targetQuote.getBid();

        BigDecimal checkBaseValue = amount.divide(quoteRate, quoteRate.scale());

        int baseValueComparing = checkBaseValue.compareTo(targetQuote.getVolumeSize());

        return baseValueComparing == 0 ? Optional.ofNullable(quoteRate) : Optional.empty();
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

        List<Quote> quotes = internalQuotesService.getQuotes(symbol);

        Quote targetQuote = findQuote(quotes, operation, amount, beneficiary);

        BigDecimal quoteRate = operation == BUY ? targetQuote.getOffer() : targetQuote.getBid();

        BigDecimal baseValue = targetQuote.getVolumeSize();

        BigDecimal checkQuotedValue = baseValue.multiply(quoteRate);

        BigDecimal bigDelta = BigDecimal.valueOf(delta);
        BigDecimal lowBorder = checkQuotedValue.subtract(bigDelta);
        BigDecimal highBorder = checkQuotedValue.add(bigDelta);
        
        if (amount.compareTo(lowBorder) >= 0 && amount.compareTo(highBorder) <= 0) {
            return Optional.ofNullable(quoteRate);
        } else {
            return Optional.empty();
        }
    }

    private Quote findQuote(List<Quote> quotes,
                            ClientOperation operation,
                            BigDecimal amount,
                            Beneficiary beneficiary) {
        BigDecimal quoteRate;
        BigDecimal targetValue;

        Quote targetQuote = quotes.get(quotes.size() - 1);
        Quote previousQuote = targetQuote;

        for (Quote quote : quotes) {
            boolean volumeOverlay = previousQuote.getVolumeSize().compareTo(quote.getVolumeSize()) == 0;

            if (volumeOverlay) {
                boolean previousOfferAbove = previousQuote.getOffer().compareTo(quote.getOffer()) > 0;
                boolean previousBidAbove = previousQuote.getBid().compareTo(quote.getBid()) > 0;

                if ((previousOfferAbove && beneficiary == BANK && operation == BUY)
                        || (previousBidAbove && beneficiary == CLIENT && operation == SELL)) {
                    quote = previousQuote;
                }
            }
            quoteRate = operation == BUY ? quote.getOffer() : quote.getBid();
            targetValue = amount.divide(quoteRate, quoteRate.scale());

            boolean targetValueLessThanQuoteValue = (targetValue.compareTo(quote.getVolumeSize()) <= 0);
            boolean targetValueMoreThanPreviousQuoteValue = (targetValue.compareTo(previousQuote.getVolumeSize()) > 0);
            boolean quoteIsInfinityAndTargetValueMoreThanMaxValue = (quote.isInfinity()
                    && targetValue.compareTo(targetQuote.getVolumeSize()) > 0);

            if (quoteIsInfinityAndTargetValueMoreThanMaxValue
                    || (targetValueLessThanQuoteValue && (targetValueMoreThanPreviousQuoteValue || volumeOverlay))) {
                targetQuote = quote;
            }
            previousQuote = quote;
        }
        return targetQuote;
    }
}