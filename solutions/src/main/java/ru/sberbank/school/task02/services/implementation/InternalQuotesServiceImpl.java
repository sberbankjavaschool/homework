package ru.sberbank.school.task02.services.implementation;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.services.InternalQuotesService;
import ru.sberbank.school.task02.exeption.EmptyQuoteList;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.util.List;

/**
 * Реализация внутреннего сервиса котировок.
 * Created by Gregory Melnikov at 28.04.2019
 */
@RequiredArgsConstructor
public class InternalQuotesServiceImpl implements InternalQuotesService {

    private final ExternalQuotesService externalQuotesService;

    /**
     * 28.04.2019
     * Получает список котировок от внешнего сервиса, выполняет проверку на наличие элементов в списке и сортировку
     *
     * @param symbol Инструмент
     * @return отсортированный список котировок
     */
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes.isEmpty()) {
            throw new EmptyQuoteList("External quotes service must return at least one quote!");
        }

        quotes.sort((previos, current) -> {
            int result = previos.getVolumeSize().compareTo(current.getVolumeSize());
            //Дополнительная сортировка при одинаковом значении объема. Сбивает тесты
//            if (result == 0) {
//                result = previos.getBid().compareTo(current.getBid());
//            }
            return result;
        });
        return quotes;
    }
}
