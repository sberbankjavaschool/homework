package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class FxConversionServiceImpl implements FxConversionService{

    List<Quote> quotes = new ArrayList<>();

    FxConversionServiceImpl(List<Quote> q){
        this.quotes = q;
    }
    
    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {

        //Создание объекта со списком котировок
        //ExternalQuotesServiceDemo externalQuotesServiceDemo = new ExternalQuotesServiceDemo();
        //List<Quote> quotes = new ArrayList<>();
        //quotes = externalQuotesServiceDemo.getQuotes(symbol);

        //Поиск первого элемента, значение которого больше amount
        int k = 0;
        while(quotes.get(k).getVolumeSize().compareTo(amount) < 0) {
            k++;
        }

        //Индекс минимального элемента, который больше amount
        int minIndex = k;
        for(int i = k; i <  quotes.size(); i++) {

            if(quotes.get(i).getVolumeSize().compareTo(amount) > 0)
            {
                if(quotes.get(i).getVolumeSize().compareTo(quotes.get(minIndex).getVolumeSize()) < 0) {
                    minIndex = i;
                }
            }
        }

        return quotes.get(minIndex).getOffer();
    }



}
