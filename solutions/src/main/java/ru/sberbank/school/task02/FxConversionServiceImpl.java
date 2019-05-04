package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.exception.FxConversionException;

import java.math.BigDecimal;


public class FxConversionServiceImpl implements FxConversionService{

    ExternalQuotesService externalQuotesService;

    FxConversionServiceImpl(ExternalQuotesService externalQuotesService){
        this.externalQuotesService = externalQuotesService;
    }

    @Override
    public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) throws FxConversionException {



        if(externalQuotesService.getQuotes(symbol).size() == 0)
            throw new FxConversionException("Список котировок ExternalQuotesService пустой");
        else {

            //Поиск первого элемента, значение которого больше amount
            int k = 0;
            while (externalQuotesService.getQuotes(symbol).get(k).getVolumeSize().compareTo(amount) < 0) {
                k++;
            }

            //Индекс минимального элемента, который больше amount
            int minIndex = k;
            for (int i = k; i < externalQuotesService.getQuotes(symbol).size(); i++) {

                if (externalQuotesService.getQuotes(symbol).get(i).getVolumeSize().compareTo(amount) > 0) {
                    if (externalQuotesService.getQuotes(symbol).get(i).getVolumeSize()
                            .compareTo(externalQuotesService.getQuotes(symbol).get(minIndex).getVolumeSize()) < 0) {
                        minIndex = i;
                    }
                }
            }

            if (operation == ClientOperation.BUY)
                return externalQuotesService.getQuotes(symbol).get(minIndex).getOffer();
            else if (operation == ClientOperation.SELL)
                return externalQuotesService.getQuotes(symbol).get(minIndex).getBid();
            else throw new FxConversionException("Недопустимая операция ClientOperation");
        }
    }
}
