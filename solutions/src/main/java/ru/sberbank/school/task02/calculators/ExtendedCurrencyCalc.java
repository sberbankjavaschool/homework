package ru.sberbank.school.task02.calculators;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExtendedCurrencyCalc extends CurrencyCalc implements ExtendedFxConversionService {

    public ExtendedCurrencyCalc(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, Beneficiary beneficiary) {
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, double delta,
                                                Beneficiary beneficiary) {
        Objects.requireNonNull(operation, "Переданный параметр operation не должен быть null");
        Objects.requireNonNull(symbol, "Переданный параметр symbol не должен быть null");
        Objects.requireNonNull(amount, "Переданный параметр amount не должен быть null");
        Objects.requireNonNull(beneficiary, "Переданный параметр beneficiary не должен быть null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Объем должен быть больше 0");
        }

        List<Quote> quotes = getExternalQuotesService().getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new FxConversionException("Список quotes равен null");
        }

        Set<Quote> exactHit = new HashSet<>();
        Set<Quote> deltaHit = new HashSet<>();
        getListsSuitableQuotes(amount, operation, exactHit, deltaHit, quotes, delta);

        if (exactHit.isEmpty() && deltaHit.isEmpty()) {
            return Optional.empty();
        }

        Set<Quote> tmp = !exactHit.isEmpty() ? exactHit : deltaHit;
        Quote find = tmp.iterator().next();
        boolean bankBenefit;

        for (Quote q : tmp) {
            bankBenefit = (operation == ClientOperation.SELL)
                    ? q.getBid().compareTo(find.getBid()) < 0
                    : q.getOffer().compareTo(find.getOffer()) > 0;

            if (beneficiary == Beneficiary.BANK && bankBenefit) {
                find = q;
            } else if (beneficiary == Beneficiary.CLIENT && !bankBenefit) {
                find = q;
            }
        }

        return Optional.of(getCurrentAmount(BigDecimal.ONE, find, operation));
    }

    /**
     * Заполняет два переданных списка значениями с точным попаданием в объем и с учетом дельты.
     *
     * @param amount объем клиента
     * @param operation покупка/продажа
     * @param exactHit список объемов с точным попаданием
     * @param deltaHit список объемов попаданий с учетом дельты
     * @param quotes список всех quotes возвращенных сервисом
     * @param delta дельта
     */
    private void getListsSuitableQuotes(BigDecimal amount, ClientOperation operation, Set<Quote> exactHit,
                                          Set<Quote> deltaHit, List<Quote> quotes, double delta) {
        for (Quote current : quotes) {
            BigDecimal exactVolume = getCurrentAmount(amount, current, operation);
            BigDecimal tmp = getSuitableVolume(amount, current, operation, delta);
            if (tmp == null) {
                continue;
            }

            boolean suit = true;

            for (Quote other : quotes) {
                if (other.getVolumeSize().compareTo(tmp) > 0 && isSuit(current, other, tmp)) {
                    suit = false;
                }
            }

            if (tmp.equals(exactVolume) && suit) {
                exactHit.add(current);
            } else if (suit) {
                deltaHit.add(current);
            }
        }
    }

    /**
     * Расчитывает три объема (точный и учетом дельты) и возвращает объем который меньше переданного quote.
     *
     * @param amount объем
     * @param quote сравниваемый quote
     * @param operation покупка/продажа
     * @param delta дельта
     * @return значение расчитанного объема, меньше переданного quote или null
     */
    private BigDecimal getSuitableVolume(BigDecimal amount, Quote quote, ClientOperation operation, double delta) {
        BigDecimal exactVolume = getCurrentAmount(amount, quote, operation);
        BigDecimal addDelta = getCurrentAmount(amount.add(BigDecimal.valueOf(delta)), quote, operation);
        BigDecimal subDelta = getCurrentAmount(amount.subtract(BigDecimal.valueOf(delta)), quote, operation);

        if (quote.isInfinity() || exactVolume.compareTo(quote.getVolumeSize()) < 0) {
            return exactVolume;
        } else if (addDelta.compareTo(quote.getVolumeSize()) < 0) {
            return addDelta;
        } else if (subDelta.compareTo(quote.getVolumeSize()) < 0) {
            return subDelta;
        } else {
            return null;
        }
    }

    /**
     * проверяет условие, что other объем меньше текущего объема и больше переданного объема.
     *
     * @param current текущий объем
     * @param other другой объем
     * @param volume объем
     * @return проверяет подходит ли другой quote текущему объему
     */
    private boolean isSuit(Quote current, Quote other, BigDecimal volume) {
        boolean currentGreater = current.isInfinity() || (current.getVolumeSize().compareTo(other.getVolumeSize()) > 0
                && current.getVolumeSize().compareTo(volume) > 0);

        return currentGreater && other.getVolumeSize().compareTo(volume) > 0;
    }

    /**
     * Делит объем клиента на цену покупки/продажи переданного quote.
     *
     * @param amount объем клиента
     * @param quote объем
     * @param operation покупка/продажа
     * @return объем исходя из переданной операции
     */
    private BigDecimal getCurrentAmount(BigDecimal amount, Quote quote, ClientOperation operation) {
        return (operation == ClientOperation.BUY)
                ? amount.divide(quote.getOffer(), 10, RoundingMode.HALF_UP)
                : amount.divide(quote.getBid(), 10, RoundingMode.HALF_UP);
    }

}
