package ru.sberbank.java.school.task02;

import java.math.BigDecimal;

import ru.sberbank.java.school.task02.util.Operation;
import ru.sberbank.java.school.task02.util.Symbol;

public interface FxConversionService {
    /**
     * Возвращает значение цены единицы базовой валюты для указанного объема.
     *
     * @param operation вид операции
     * @param symbol    Инструмент
     * @param amount    Объем
     * @return Цена для указанного объема
     */
    BigDecimal convert(Operation operation, Symbol symbol, BigDecimal amount);

}
