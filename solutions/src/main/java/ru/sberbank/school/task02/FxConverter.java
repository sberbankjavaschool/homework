package ru.sberbank.school.task02;

import java.math.BigDecimal;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class FxConverter implements FxConversionService {

    private ExternalQuotesService quotesService;

    FxConverter(ExternalQuotesService q) {
        quotesService = q;
    }

    /**
     * Функция обрабатывает список запросов и возвращает список ответов.
     *
     * @param operation операция(направление транзакции)
     * @param symbol    котируемая валюта
     * @param amount    количесво валютыs
     * @throws IllegalArgumentException если amount не является числом, либо amount <= 0
     * @throws NullPointerException     если кокой-либо аргумент null
     * @throws FxConversionException    если лист котировок пуст
     */
    @Override
    public BigDecimal convert(@NonNull ClientOperation operation, @NonNull Symbol symbol, @NonNull BigDecimal amount)
            throws FxConversionException {

        if (amount == null) {
            throw new NullPointerException("Amount is null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount less than 0");
        }

        if (quotesService.getQuotes(symbol) == null || quotesService.getQuotes(symbol).isEmpty()) {
            throw new FxConversionException("Quotes list is null or empty");
        }

        Quote curQuote = null;
        BigDecimal curVolume = amount;

        for (Quote quote : quotesService.getQuotes(symbol)) {
            BigDecimal volume = quote.getVolumeSize();
            if (amount.compareTo(volume) < 0 || quote.isInfinity()) {
                if (curVolume.compareTo(amount) <= 0 || curQuote.isInfinity()
                        || (volume.compareTo(curVolume) < 0 && !quote.isInfinity())) {
                    curQuote = quote;
                    curVolume = volume;
                }
            }
        }

        if (curQuote == null) {
            throw new FxConversionException("No suitable quotes");
        }

        return (operation == ClientOperation.BUY) ? curQuote.getOffer() : curQuote.getBid();
    }

}
