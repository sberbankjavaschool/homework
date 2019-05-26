package ru.sberbank.school.task02;

import org.apache.commons.cli.Options;
import ru.sberbank.school.task02.exception.*;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ClientController implements FxClientController {

    private FxConversionService calculator;

    public ClientController(FxConversionService calculator) {
        this.calculator = calculator;
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {

        List<FxResponse> responses = new ArrayList<>();

        for (FxRequest request : requests) {
            responses.add(fetchResult(request));
        }

        return responses;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {

        if (requests == null) {
            throw new ConverterConfigurationException("invalid request");
        }

        String symbol;
        String direction;
        String amount;
        String date = LocalDateTime.now().toString();

        try {

            symbol = checkSymbol(requests.getSymbol());
            direction = checkDirection(requests.getDirection());
            amount = checkDecimal(requests.getAmount());
        } catch (ConverterConfigurationException | NullPointerException cce) {
            throw new ConverterConfigurationException(cce.getMessage());
        }

        try {

            BigDecimal price = calculator.convert(getDirection(direction),getSymbol(symbol), new BigDecimal(amount))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);

            return FxResponse.builder()
                    .notFound(false)
                    .symbol(symbol)
                    .price(price.toString())
                    .amount(amount)
                    .date(date)
                    .direction(direction)
                    .build();
        } catch (FxConversionException ce) {

            System.err.println(ce.getMessage());

            return FxResponse.builder()
                    .notFound(true)
                    .symbol(symbol)
                    .price(null)
                    .amount(amount)
                    .date(date)
                    .direction(direction)
                    .build();
        }


    }

    public FxRequest makeRequest(String[] args) {

        try {

            Map<String, String> parsedArgs = new ArgumentParser(provideOptions()).parseArgs(args);

            return new FxRequest(parsedArgs.get("s"), parsedArgs.get("d"), parsedArgs.get("a"));
        } catch (NullPointerException | ConverterConfigurationException e) {
            throw new ConverterConfigurationException(e.getMessage());
        }
    }

    private String checkSymbol(String symbol) {

        if (symbol == null) {
            throw new NullPointerException("symbol parameter missing");
        }

        symbol = symbol.replace("/", "_");

        if (symbol.equalsIgnoreCase("RUB_USD")) {
            return "RUB_USD";
        } else if (symbol.equalsIgnoreCase("USD_RUB")) {
            return "USD_RUB";
        } else {
            throw new WrongSymbolException("invalid symbol: specify currency (USD/RUB, RUB/USD, etc.)");
        }
    }

    private String checkDirection(String direction) {

        if (direction == null) {
            throw new NullPointerException("operation parameter missing");
        }

        if (direction.equalsIgnoreCase("BUY")) {
            return "BUY";
        } else if (direction.equalsIgnoreCase("SELL")) {
            return "SELL";
        } else {
            throw new ConverterConfigurationException("invalid operation: choose 'BUY' or 'SELL'");
        }
    }

    private String checkDecimal(String value) {

        if (value == null) {
            throw new NullPointerException("amount parameter missing");
        }

        try {
            return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } catch (NumberFormatException nfe) {
            throw new ConverterConfigurationException("incorrect value: should be positive decimal");
        }
    }

    private Symbol getSymbol(String symbol) {
        return symbol.equals("RUB_USD") ? Symbol.RUB_USD : Symbol.USD_RUB;
    }

    private ClientOperation getDirection(String direction) {
        return direction.equals("SELL") ? ClientOperation.SELL : ClientOperation.BUY;
    }

    public Options provideOptions() {

        Options o = new Options();

        o.addOption("s", "symbol", true, "Currency symbol");
        o.addRequiredOption("d", "direction", true, "Desired operation");
        o.addRequiredOption("a", "amount", true, "Desired amount");

        return o;
    }
}
