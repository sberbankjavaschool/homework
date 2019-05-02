package ru.sberbank.school.task02.services;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.util.List;
import java.util.Map;

/**
 * Внутренний сервис подготавливает данные приходящие из внешнего сервиса
 * Created by Gregory Melnikov at 28.04.2019
 */
public interface InternalQuotesService {

    List<Quote> getQuotes(Symbol symbol);

}

