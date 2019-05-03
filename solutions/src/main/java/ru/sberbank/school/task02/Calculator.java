package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

/**
 * Реализация валютного калькулятора. Калькулятор определяет значение цены еденицы базовой валюты
 *  для заданного количества котируемой валюты.
 */
public class Calculator implements FxConversionService {

    protected ExternalQuotesService externalQuotesService;

    public Calculator(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    /**
     * Возвращает стоимость единицы базовой валюты для зданного объема.
     * @param operation Вид операции (BUY, SELL)
     * @param symbol    Инструмент (USD/RUB: USD - базовая валюта, RUB - котиремая валюта)
     * @param amount    Объем
     * @return Цена для указанного объема
     */
    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {

        if (operation == null) {
            throw new NullPointerException("Parameter operation is null!");
        }
        if (symbol == null) {
            throw new NullPointerException("Parameter symbol is null!");
        }
        if (amount == null) {
            throw new NullPointerException("Parameter amount is null!");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount less than or equal to 0!");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);
        if (quotes == null || quotes.isEmpty()) {
            throw new EmptyQuoteException("No quotes!");
        }

        BigDecimal volume = BigDecimal.valueOf(0);
        Quote currentQuote = null;

        for (Quote q : quotes) {
            if (volumeCompare(amount, q.getVolumeSize()) < 0) {
                if (volumeCompare(volume, q.getVolumeSize()) > 0 || volume.compareTo(BigDecimal.ZERO) == 0) {
                    volume = q.getVolumeSize();
                    currentQuote = q;
                }
            }
        }
        return operation == ClientOperation.BUY ? currentQuote.getOffer() : currentQuote.getBid();

    }

    /**
     * Метод для сравнения двух значений с учетом, что -1=Infinity.
     * @param d1 первое число
     * @param d2 второе число
     * @return -1, 0, 1 согласно методу compareTo
     */
    private int volumeCompare(BigDecimal d1, BigDecimal d2) {
        if (d1.compareTo(BigDecimal.ZERO) < 0) {
            return 1;
        }

        if (d2.compareTo(BigDecimal.ZERO) < 0) {
            return -1;
        }

        return d1.compareTo(d2);
    }
}