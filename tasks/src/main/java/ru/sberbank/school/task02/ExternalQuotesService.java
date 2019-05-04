package ru.sberbank.school.task02;

import java.util.List;

import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

/**
 * Поставщик данных о котировках. О его имплементации ничего не известно.
 * Реализовать его мог как психически нестабильный человек, так и просто далёкий от программирования.
 */
public interface ExternalQuotesService {

    List<Quote> getQuotes(Symbol symbol);
}
