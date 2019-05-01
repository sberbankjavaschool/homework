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

import static ru.sberbank.school.task02.util.Beneficiary.*;
import static ru.sberbank.school.task02.util.ClientOperation.BUY;
import static ru.sberbank.school.task02.util.ClientOperation.SELL;

/**
 * Обратный валютный кальклятор: возвращает значение цены единицы котируемой валюты для заданного объема базовой.
 * Для параметров SELL, USD/RUB, 1000, CLIENT, ситуация читается так:
 * Клиент хочет продать долларов на 1000 рублей, сказать сколько будет стоить 1 доллар.
 * Created by Gregory Melnikov at 01.05.2019
 */
public class ExtendedFxConversionServiceImpl extends FxConversionServiceImpl implements ExtendedFxConversionService {

    private final InternalQuotesService internalQuotesService;

    @java.beans.ConstructorProperties({"internalQuotesService"})
    public ExtendedFxConversionServiceImpl(InternalQuotesService internalQuotesService) {
        super(internalQuotesService);
        this.internalQuotesService = internalQuotesService;
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

        BigDecimal quoteRate = amount;
        BigDecimal targetValue = amount;

        Quote targetQuote = quotes.get(quotes.size() - 1);
        Quote previousQuote = targetQuote;
//        boolean volumeOverlay= false;
        System.out.println("targetQuote:" + targetQuote.getVolumeSize());

        for (Quote quote : quotes) {
            boolean volumeOverlay = previousQuote.getVolumeSize().compareTo(quote.getVolumeSize()) == 0;

            if (volumeOverlay) {
                boolean previousOfferAbove = previousQuote.getOffer().compareTo(quote.getOffer()) > 0;
                boolean previousBidAbove = previousQuote.getBid().compareTo(quote.getBid()) > 0;

                if ((previousOfferAbove && BANK.equals(beneficiary) && BUY.equals(operation))
                        || (previousBidAbove && CLIENT.equals(beneficiary) && SELL.equals(operation))) {
                    quote = previousQuote;
                }
            }
//            BigDecimal quoteRate = BUY.equals(operation) ? quote.getOffer() : quote.getBid();
//            BigDecimal target = amount.divide(quoteRate, quoteRate.scale());

            quoteRate = BUY.equals(operation) ? quote.getOffer() : quote.getBid();
            targetValue = amount.divide(quoteRate, quoteRate.scale());

            System.out.println("quoteRate:" + quoteRate.toString());
            System.out.println("targetValue:" + targetValue.toString());
            System.out.println("getVolumeSize:" + quote.getVolumeSize());


//            if (targetQuoteFounded) break;
//            targetQuoteFounded = quoteComparator(quote, targetQuote, targetValue);

            if (quote.isInfinity() && targetValue.compareTo(targetQuote.getVolumeSize()) > 0) {
                targetQuote = quote;
//                break;
            }
//            if (targetQuote.isInfinity() && targetValue.compareTo(quote.getVolumeSize()) > 0) {
//                break;
//            }
            if (targetValue.compareTo(quote.getVolumeSize()) < 0) {
                targetQuote = quote;
//                break;
            }
        }
        System.out.println("targetQuote:" + targetQuote.toString());

        quoteRate = BUY.equals(operation) ? targetQuote.getOffer() : targetQuote.getBid();
        BigDecimal checkValue = quoteRate.multiply(targetValue);
        System.out.println(checkValue.toString());

        return checkValue.compareTo(amount) == 0 ? Optional.ofNullable(quoteRate) : Optional.empty();
        //return Optional.ofNullable(quoteRate);
//        return result;
//        return Optional.empty();
    }

    /**
     * Возвращает значение цены единицы котируемой валюты для указанного объема.
     *
     * @param operation   вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param delta       допустимое отклонение при отсутствии точного попадания
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

        BigDecimal divisor;
        BigDecimal targetValue = new BigDecimal(0);

        Quote targetQuote = quotes.get(quotes.size() - 1);

        for (Quote quote : quotes) {
            divisor = BUY.equals(operation) ? quote.getOffer() : quote.getBid();
            targetValue = amount.divide(divisor, divisor.scale());

            if (quote.isInfinity() && targetValue.compareTo(targetQuote.getVolumeSize()) > 0) {
                targetQuote = quote;
                break;
            }
            if (targetQuote.isInfinity() && targetValue.compareTo(quote.getVolumeSize()) > 0) {
                break;
            }
            if (targetValue.compareTo(quote.getVolumeSize()) < 0) {
                targetQuote = quote;
                break;
            }
        }
        BigDecimal quoteRate = BUY.equals(operation) ? targetQuote.getOffer() : targetQuote.getBid();
        BigDecimal checkValue = quoteRate.multiply(targetValue);

        BigDecimal bDelta = BigDecimal.valueOf(delta);
        BigDecimal lowBorder = amount.subtract(bDelta);
        BigDecimal hightBorder = amount.add(bDelta);

        System.out.println("lowBorder:" + lowBorder);
        System.out.println("hightBorder:" + hightBorder);
        System.out.println("quoteRate:" + checkValue);
        System.out.println(checkValue.compareTo(lowBorder));

        if (checkValue.compareTo(lowBorder) >= 0 && checkValue.compareTo(hightBorder) <= 0) {
            return Optional.ofNullable(quoteRate);
        } else {
            return Optional.empty();
        }
//        return quoteRate.compareTo(lowBorder) >= 0 && quoteRate.compareTo(hightBorder) <= 0 ?
//                Optional.ofNullable(quoteRate) : Optional.empty();
    }
}

