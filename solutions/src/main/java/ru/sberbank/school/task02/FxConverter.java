package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.List;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.Volume;

public class FxConverter implements FxConversionService {

  private ExternalQuotesService Quotes;

  FxConverter(ExternalQuotesService q) {
    Quotes = q;
  }

  @Override
  public BigDecimal convert(ClientOperation operation, Symbol symbol, BigDecimal amount) {
    if (operation == null) {
      throw new NullPointerException("Operation is null");
    }

    if (symbol == null) {
      throw new NullPointerException("Symbol is null");
    }

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount less than 0");
    }


    Quote curQuote = null;
    BigDecimal curVolume = amount;

    for (Quote quote : Quotes.getQuotes(symbol)) {
      BigDecimal volume = quote.getVolumeSize();
      if (amount.compareTo(volume) < 0) {
        if ((curVolume.compareTo(amount) <= 0) || (volume.compareTo(curVolume) < 0)) {
          curVolume = volume;
          curQuote = quote;
        }
      }
    }
      System.out.println(curVolume);
    return (operation == ClientOperation.BUY) ? curQuote.getOffer() : curQuote.getBid();
  }
}
