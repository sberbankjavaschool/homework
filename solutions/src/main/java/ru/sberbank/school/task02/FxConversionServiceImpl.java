package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.exception.FxConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class FxConversionServiceImpl implements FxConversionService{

    ExternalQuotesService externalQuotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService){
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) throws FxConversionException {

        List<Quote> quotes = new ArrayList<>();
        quotes = externalQuotesService.getQuotes(symbol);

        if(quotes.size() == 0)
            throw new FxConversionException("Список котировок ExternalQuotesService пустой");
        else {

            //Поиск первого элемента, значение которого больше amount
            int k = 0;
            while (quotes.get(k).getVolumeSize().compareTo(amount) < 0 &&
                    k < quotes.size() - 1) {
                k++;
            }

            //Индекс минимального элемента, который больше amount
            int minIndex = k;
            for (int i = k; i < quotes.size(); i++) {

                if (quotes.get(i).getVolumeSize().compareTo(amount) > 0) {
                    if (quotes.get(i).getVolumeSize().compareTo(quotes.get(minIndex).getVolumeSize()) < 0) {
                        minIndex = i;
                    }
                }
            }


            if (operation == ClientOperation.BUY) {
                return quotes.get(minIndex).getOffer();
            }
            else if (operation == ClientOperation.SELL) {
                return quotes.get(minIndex).getBid();
            }
            else throw new FxConversionException("Недопустимая операция ClientOperation");
        }
    }
}
