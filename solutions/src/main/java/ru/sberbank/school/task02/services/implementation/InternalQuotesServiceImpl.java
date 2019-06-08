package ru.sberbank.school.task02.services.implementation;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.services.InternalQuotesService;
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

        if (quotes == null) {
            throw new FxConversionException("Quotes not found for symbol " + symbol);
        }

        quotes.sort((previous, next) -> {
            int result = previous.getVolumeSize().compareTo(next.getVolumeSize());
            if (result == 0) {
                result = next.getBid().compareTo(previous.getBid());
            }
            return result;
        });
        return quotes;
    }
}