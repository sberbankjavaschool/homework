package ru.sberbank.school.task02;

import org.apache.commons.cli.HelpFormatter;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {

        ExternalQuotesService quotes = new ExternalQuotesServiceDemo();
        ServiceFactory factory = new CurrencyCalcFactory();
        FxConversionService calculator = factory.getFxConversionService(quotes);
        ExtendedFxConversionService reverseCalculator = factory.getExtendedFxConversionService(quotes);
        ClientController client = new ClientController(calculator);

        try {

            FxRequest request = client.makeRequest(args);
            FxResponse response = client.fetchResult(request);

            System.out.println(response);

            Optional<BigDecimal> price = reverseCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                    new BigDecimal(7_750_000), Beneficiary.CLIENT);
            Optional<BigDecimal> price2 = reverseCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                    new BigDecimal(0.5), Beneficiary.BANK);
            Optional<BigDecimal> price3 = reverseCalculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                    new BigDecimal(7_500), Beneficiary.CLIENT);

            System.out.println(price);
            System.out.println(price2);
            System.out.println(price3);
        } catch (FxConversionException | NullPointerException e) {

            System.err.println(e.getMessage());
            new HelpFormatter().printHelp("Currency calculator client: ", client.provideOptions());
            System.exit(1);
        }
    }

}
