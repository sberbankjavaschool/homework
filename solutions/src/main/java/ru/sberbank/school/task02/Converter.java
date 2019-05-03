package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

public class Converter implements FxConversionService {
    private ExternalQuotesService externalQuotesService;

    public Converter(ExternalQuotesService externalQuotesService) {
        this.externalQuotesService = externalQuotesService;
    }


    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
        if (operation == null || symbol == null || amount == null) {
            throw new NullPointerException();
        }

        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Введите корректное значение объема!");
        }

        List<Quote> quotes = externalQuotesService.getQuotes(symbol);

        if (quotes.isEmpty()) {
            throw new FxConversionException("Список валют пуст!");
        }

        Quote result = quotes.stream()
                .filter(q -> amount.compareTo(q.getVolumeSize()) < 0).min((q1,q2) -> {
                    if (q1.isInfinity()) {
                        return 1;
                    }

                    if (q2.isInfinity()) {
                        return -1;
                    }

                    return q1.getVolumeSize().compareTo(q2.getVolumeSize());
                }).orElse(quotes.get(0));

        boolean isSell = operation == ClientOperation.SELL;
        return isSell ? result.getBid() : result.getOffer();
  }


}
