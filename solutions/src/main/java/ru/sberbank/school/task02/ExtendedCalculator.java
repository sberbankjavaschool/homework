package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import ru.sberbank.school.task02.util.*;
import ru.sberbank.school.task02.exception.EmptyQuoteException;

/**
 * Реализация обратного валютного калькулятора. Калькулятор определяет значение цены котируемой валюты
 * для заданного количества.
 */
public class ExtendedCalculator extends Calculator implements ExtendedFxConversionService {

    public ExtendedCalculator(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                Beneficiary beneficiary) {
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    /**
     * Возвращает значение цены единицы котируемой валюты для указанного объема.
     *
     * @param operation   Вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param delta       Допустимое отклонение при отсутствии точного попадания в котируемой валюте
     * @param beneficiary В чью пользу осуществляется округление
     * @return Цена для указанного объема
     * @throws IllegalArgumentException если amount <= 0
     * @throws EmptyQuoteException если нет котировок или amount не попало ни в один из диапазонов
     */
    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount,
                                                double delta, Beneficiary beneficiary) {

        Objects.requireNonNull(operation, "Parameter operation must be not null");
        Objects.requireNonNull(symbol, "Parameter symbol must be not null");
        Objects.requireNonNull(amount, "Parameter amount must be not null");
        Objects.requireNonNull(beneficiary, "Parameter beneficiary must be not null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount less than or equal to 0!");
        }

        // Получаем список котировок для базовой валюты
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new EmptyQuoteException("No quotes!");
        }

        // Получаем список котировок для котируемой валюты
        List<ReverseQuote> reverseQuotes = getReverseQuotes(quotes, operation);
        List<BigDecimal> prices = new ArrayList<>();
        List<Diff> diff = new ArrayList<>();

        // Находим диапазон, в который входит amount, и добавляем найденную цену в список.
        for (ReverseQuote r : reverseQuotes) {
            if ((r.getVolumeFrom().isInfinity() || amount.compareTo(r.getVolumeFromSize()) >= 0)
                    && (r.getVolumeTo().isInfinity() || amount.compareTo(r.getVolumeToSize()) < 0)) {
                prices.add(r.getPrice());
            }
        }

        // Если amount не попало ни в один диапазон, то проверяем попадание amount +- delta
        if (prices.isEmpty() && delta != 0) {
            BigDecimal d = BigDecimal.valueOf(delta);
            for (ReverseQuote r : reverseQuotes) {
                Volume a = r.getVolumeFrom();
                Volume b = r.getVolumeTo();
                BigDecimal lowAmount = amount.subtract(d);
                BigDecimal highAmount = amount.add(d);

                if (isCross(a, b, lowAmount, highAmount)) {
                    // Сохраняем найденную цену и минимальное отклонение
                    diff.add(new Diff(a.getVolume(), b.getVolume(), amount));
                    prices.add(r.getPrice());
                }
            }
        }

        if (prices.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(diff.isEmpty() ? findSuitablePrice(prices, beneficiary, operation) :
                findSuitablePrice(diff, prices, beneficiary, operation));

    }

    /**
     * Проверяет, пересекаются ли интервалы [a, b) и [lowAmount, highAmount].
     * @param a          Начало первого интервала
     * @param b          Конец первого интервала (не включительно)
     * @param lowAmount  Начало второго интервала
     * @param highAmount Конец второго интервала (включительно)
     * @return true - если интервалы пересекаются, иначе - false
     */
    private boolean isCross(Volume a, Volume b,BigDecimal lowAmount, BigDecimal highAmount) {

        return !a.isInfinity() && lowAmount.compareTo(a.getVolume()) <= 0 && a.getVolume().compareTo(highAmount) <= 0
                || !b.isInfinity() && lowAmount.compareTo(b.getVolume()) < 0
                && b.getVolume().compareTo(highAmount) < 0;

    }

    /**
     * Находит цену с учетом параметров beneficiary и operation.
     * @param prices      Список цен
     * @param beneficiary В чью пользу осуществляется округление
     * @param operation   Вид операции
     * @return Цена с учетом параметров beneficiary и operation
     */
    private BigDecimal findSuitablePrice(List<BigDecimal> prices, Beneficiary beneficiary, ClientOperation operation) {

        BigDecimal suitablePrice = prices.get(0);

        for (int i = 1; i < prices.size(); i++) {
            suitablePrice  = selectPrice(suitablePrice, prices.get(i), beneficiary, operation);
        }

        return suitablePrice;

    }

    /**
     * Находит минимальное отклонение, а потом выбирает подходящую цену.
     * @param diff        Список отклонений
     * @param prices      Список цен
     * @param beneficiary В чью пользу осуществляется округление
     * @param operation   Вид операции
     * @return Цена для минимального отклонения с учетом параметров beneficiary и operation
     */
    private BigDecimal findSuitablePrice(List<Diff> diff, List<BigDecimal> prices, Beneficiary beneficiary,
                                         ClientOperation operation) {

        BigDecimal suitablePrice = prices.get(0);
        Diff minDiff = diff.get(0);

        for (int i = 1; i < prices.size(); i++) {
            if (diff.get(i).compareTo(minDiff) < 0) {
                suitablePrice = prices.get(i);
                minDiff = diff.get(i);
            } else if (diff.get(i).compareTo(minDiff) == 0) {
                suitablePrice = selectPrice(suitablePrice, prices.get(i), beneficiary, operation);
            }
        }

        return  suitablePrice;
    }

    /**
     * Выбирает цену на основе параметров beneficiary и operation.
     * @param price        Первая цена
     * @param anotherPrice Вторая цена
     * @param beneficiary  В чью пользу осуществляется округление
     * @param operation    Вид операции
     * @return Цена, подходящая по условию
     */
    private BigDecimal selectPrice(BigDecimal price, BigDecimal anotherPrice, Beneficiary beneficiary,
                                   ClientOperation operation) {

        // CLIENT&SELL || BANK&BUY - максимальная цена, CLIENT&BUY || BANK&SELL - минимальная цена
        boolean cond = beneficiary == Beneficiary.CLIENT ^ operation == ClientOperation.BUY;

        return cond && price.compareTo(anotherPrice) < 0 || !cond && price.compareTo(anotherPrice) > 0
                ? anotherPrice : price;

    }

    private void sortQuotes(List<Quote> quotes) {
        quotes.sort((q1, q2) -> {
            if (q1.isInfinity()) {
                return 1;
            }
            if (q2.isInfinity()) {
                return -1;
            }
            return q1.getVolumeSize().compareTo(q2.getVolumeSize());
        });
    }

    /**
     * Вычисляет котировки для котируемй валюты.
     * @param quotes    Список котировок для базовой валюты
     * @param operation Вид операции
     * @return Список котировок для котируемй валюты
     */
    private List<ReverseQuote> getReverseQuotes(List<Quote> quotes, ClientOperation operation) {

        sortQuotes(quotes);
        List<ReverseQuote> reverseQuotes = new ArrayList<>();

        Iterator<Quote> iterator = quotes.iterator();
        Quote qPrev;
        Quote q = null;

        while (iterator.hasNext()) {
            qPrev = q;
            q = iterator.next();

            BigDecimal price = operation == ClientOperation.SELL
                    ? q.getBid() : q.getOffer();
            Volume volumeFrom = qPrev == null
                    ? Volume.from(0) : Volume.from(qPrev.getVolumeSize().multiply(price));
            Volume volumeTo = Volume.from(q.getVolumeSize().multiply(price));
            price = BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);
            ReverseQuote r = new ReverseQuote(volumeFrom, volumeTo, price);
            reverseQuotes.add(r);
        }

        return reverseQuotes;
    }

}

