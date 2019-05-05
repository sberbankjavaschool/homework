package ru.sberbank.school.task02.exception;

import lombok.NonNull;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxConversionService;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class FxConversionServiceImpl implements FxConversionService {
  private ExternalQuotesService externalQuotesService;

  private ClientOperation operation;

  public FxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
    this.externalQuotesService = externalQuotesService;
  }

  @Override
  public BigDecimal convert(@NonNull ClientOperation operation, @NonNull Symbol symbol,
                            @NonNull BigDecimal amount) {
    this.operation = operation;
    if (checkCorrectConvert(amount)) {
      throw new ConverterConfigurationException("Amount меньше или равно нулю");
    }
    List<Quote> quotes = externalQuotesService.getQuotes(symbol);
    if (quotes.isEmpty()) {
      throw new ConverterConfigurationException("externalQuotesService пустой");
    }

    Quote bestQuotes = quotes.get(0);
    for (Quote quote : quotes) {
      if (amount.compareTo(quote.getVolumeSize()) < 0 || quote.isInfinity()) {
        if (operationPrice(bestQuotes).compareTo(operationPrice(quote)) > 0) {
          bestQuotes = quote;
        }
      }
    }
    return operationPrice(bestQuotes);
  }

  private boolean checkCorrectConvert(BigDecimal amount) {
    boolean state = false;
    if (amount.compareTo(BigDecimal.ZERO) > 0) {
      state = true;
    }
    return  state;
  }

  private BigDecimal operationPrice(Quote quote) {
    return this.operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid();
  }
}