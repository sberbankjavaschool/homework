package ru.sberbank.school.task02;

import java.math.BigDecimal;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.Symbol;

public class FxConverterFactory implements ServiceFactory {
  @Override
  public FxConversionService getFxConversionService(ExternalQuotesService externalQuotesService) {
    return new FxConverter(externalQuotesService);
  }

  public static void main(String[] args) {
    FxConversionService converter = new FxConverter(new ExternalQuotesServiceDemo());
    System.out.println(converter.convert(ClientOperation.BUY, Symbol.USD_RUB, new BigDecimal(1000000)));
  }
}
