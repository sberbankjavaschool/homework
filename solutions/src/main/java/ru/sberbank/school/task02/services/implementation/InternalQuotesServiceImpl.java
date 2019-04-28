package ru.sberbank.school.task02.services.implementation;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.services.InternalQuotesService;
import ru.sberbank.school.task02.services.exeption.EmptyQuoteList;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

import java.math.BigDecimal;
import java.util.*;

/**
 * Реализация внутреннего сервиса котировок.
 *
 * Created by Gregory Melnikov at 28.04.2019
 */
@RequiredArgsConstructor
public class InternalQuotesServiceImpl implements InternalQuotesService {
    private final ExternalQuotesService externalQuotesService;

    /**
     * Получает список котировок от внешнего сервиса, сортирует и упаковывает в TreeMap
     * @param symbol
     * @return TreeMap, ключ - объем, значение - котировка
     */
    @Override
    public List<Quote> getQuotes(Symbol symbol) {
        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes.isEmpty()) {
            throw new EmptyQuoteList("External quotes service must return at least one quote!");
        }

        Map<Volume, Quote> mappedQuotes = new HashMap<>();

        List<Quote> quotes2 = new ArrayList<>();
/*        quotes.sort((previos, current) -> {
            if (current.isInfinity()) {
                System.out.println("current YES INFINITY " + current.toString() + previos);
//                return 1;
                return previos.getBid().compareTo(current.getBid());
//                return current.getBid().compareTo(previos.getBid());

            }
            if (previos.isInfinity()) {
                System.out.println("current NO INFINITY " + current.toString() + previos);
//                return 1;
                return current.getBid().compareTo(previos.getBid());
//                return previos.getBid().compareTo(current.getBid());
            }
//            if (previos.isInfinity()) {
////                System.out.println("previos YES INFINITY " + previos.toString() + current);
//                previos.getVolumeSize().compareTo(current.getVolumeSize());
//                return 0;
//            }
            System.out.print("previos " + previos.getVolumeSize() + "\t");
            System.out.print("CURRENT " + current.getVolumeSize() + "\t");
            System.out.println(previos.getVolumeSize().compareTo(current.getVolumeSize()));
            return previos.getVolumeSize().compareTo(current.getVolumeSize());
        });*/

        quotes.sort((previos, current) -> {
            int result = previos.getVolumeSize().compareTo(current.getVolumeSize());
            if (result == 0) {
                result = previos.getBid().compareTo(current.getBid());
            }
            return result;
        });

//        quotes.sort((previos, next) -> previos.getVolumeSize().compareTo(next.getVolumeSize()));

//        quotes.sort((o1, o2) -> {
//            if (o1.isInfinity()) {
//                return 1;
//            }
//            if (o2.isInfinity()) {
//                return 0;
//            }
//            return o1.getVolumeSize().compareTo(o2.getVolumeSize());
//        });

        for (Quote quote : quotes2) {
            System.out.println("2");
            System.out.println(quote.toString());
        }

        for (Quote quote : quotes) {
            System.out.println("1");
            System.out.println(quote.getVolumeSize());
            System.out.println(quote.toString());
            mappedQuotes.put(quote.getVolume(), quote);

        }

        return quotes;
    }
}
