package ru.sberbank.school.task02.calculator;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Quote;

import java.util.List;

public class TestList {
    public static void main(String[] args) {
        CurrencyCalculator service = new CurrencyCalculator();
        List<Quote> list = service.filterQutesList();
        for (Quote quote : list) {
            System.out.println(quote.getVolume().getVolume());
        }
    }
}
