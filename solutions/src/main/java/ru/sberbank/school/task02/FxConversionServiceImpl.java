package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.exception.FxConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FxConversionServiceImpl implements FxConversionService {

    ExternalQuotesService externalQuotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount)
            throws FxConversionException, IllegalArgumentException {

        if (operation == null) {
            throw new IllegalArgumentException("Аргумент operation не может быть null");
        }

        if (symbol == null) {
            throw new IllegalArgumentException("Аргумент symbol не может быть null");
        }

        if (amount == null || amount.compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            throw new IllegalArgumentException("Аргумент amount должен быть больше 0 и не null");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes == null || quotes.size() == 0) {
            throw new FxConversionException("Список котировок ExternalQuotesService не должен быть пустой");
        }


        /**
         * Цикл проходит по массиву и ищет два индекса:
         * - индекс минимального объема volume (minIndex)
         * - индекс минимального минимального объема volume, который больше amount (minVolumeIndex)
         * Если amount больше самого большого объема volume и для него нет котировки
         * возвращается цена минимального объема валюты
         *
         * @param amountAsDouble    объем валюты, которую запрашивает клиент (тип Double)
         * @param tempValue         временная переменная, минимальное значение volume,
         *                          которое больше amount на текущем шаге цикла
         * @param minVolume         минимальный объем, который предлагает банк
         * @param minVolumeIndex    индекс, которому соотвествует минимальный объем в списке quotes,
         *                          который больше amount
         * @param minIndex          индекс, которому соотвествует минимальный объем в списке quotes
         * @param index             итоговый индекс котировки, которая возвращается методом
         * @return                  котировка для заданного объема amount
         */

        double amountAsDouble = amount.doubleValue();
        double tempValue = Double.MAX_VALUE;
        double minVolume = quotes.get(0).getVolumeSize().doubleValue();

        int minVolumeIndex = 0;
        int minIndex = 0;
        int index = 0;

        for (int i = 0; i < quotes.size(); i++) {

            double value = quotes.get(i).getVolumeSize().doubleValue();

            if (value > 0 && value < minVolume) {
                minVolume = value;
                minIndex = i;
            }

            if (value > 0) {
                if (value > amountAsDouble) {
                    if (value < tempValue) {
                        tempValue = value;
                        minVolumeIndex = i;
                    }
                }
            }
        }

        index = quotes.get(minVolumeIndex).getVolumeSize().compareTo(amount) > 0 ? minVolumeIndex : minIndex;

        if (operation == ClientOperation.BUY) {
            return quotes.get(index).getOffer();
        } else if (operation == ClientOperation.SELL) {
            return quotes.get(index).getBid();
        } else {
            throw new FxConversionException("Недопустимая операция ClientOperation");
        }

    }
}