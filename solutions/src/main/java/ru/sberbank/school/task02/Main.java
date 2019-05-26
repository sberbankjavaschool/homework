package ru.sberbank.school.task02;


import org.apache.commons.cli.HelpFormatter;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public class Main {
    public static void main(String[] args) {

        ExternalQuotesService quotes = new ExternalQuotesServiceDemo();
        ServiceFactory factory = new CurrencyCalcFactory();
        FxConversionService calculator = factory.getFxConversionService(quotes);
        ClientController client = new ClientController(calculator);
        HelpFormatter hf = new HelpFormatter();

        try {

            FxRequest request = client.makeRequest(args);
            FxResponse response = client.fetchResult(request);

            System.out.println(response);
        } catch (FxConversionException cce) {

            System.err.println(cce.getMessage());
            hf.printHelp("Usage: ", client.provideOptions());
            System.exit(1);
        }
    }

}
