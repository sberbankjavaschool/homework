package ru.sberbank.school.task02;

import org.apache.commons.cli.CommandLine;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ClientController implements FxClientController {

    private FxConversionService calculator;
    private ArgumentParser parser;

    public ClientController(FxConversionService calculator) {
        this.calculator = calculator;
        this.parser = new ArgumentParser();
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

        String symbol = formatSymbol(requests.getSymbol());
        String direction = formatDirection(requests.getDirection());
        String amount = formatDecimal(requests.getAmount());
        String date = LocalDate.now().toString();

        BigDecimal price = calculator.convert(getDirection(direction),
                                              getSymbol(symbol),
                                          new BigDecimal(amount));

        return price == null ?
                        new FxResponse(symbol, null, amount,
                                       date, direction, true) :
                        new FxResponse(symbol, formatDecimal(price.toString()),
                                       amount, date, direction, false);

    }

    public FxRequest makeRequest(String[] args) {

        CommandLine cmd = parser.parseArgs(args);

        return cmd == null ? null :
                             new FxRequest(cmd.getOptionValue("s"),
                                           cmd.getOptionValue("d"),
                                           cmd.getOptionValue("a"));
    }

    private String formatSymbol(String symbol) {

        symbol = symbol.replace("/", "_");

        if (symbol.equalsIgnoreCase("RUB_USD")) {
            return "RUB_USD";
        } else if (symbol.equalsIgnoreCase("USD_RUB")) {
            return "USD_RUB";
        } else {
            throw new WrongSymbolException("invalid symbol");
        }
    }

    private String formatDirection(String direction) {

        if (direction.equalsIgnoreCase("BUY")) {
            return "BUY";
        } else if (direction.equalsIgnoreCase("SELL")) {
            return "SELL";
        } else {
            throw new FxConversionException("invalid operation");
        }
    }

    private String formatDecimal(String value) {

        try {
            return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } catch (NumberFormatException nfe) {
            throw new FxConversionException("incorrect amount value");
        }

    }

    private Symbol getSymbol(String symbol) {
        return symbol.equals("RUB_USD") ? Symbol.RUB_USD : Symbol.USD_RUB;
    }

    private ClientOperation getDirection(String direction) {
        return direction.equals("SELL") ? ClientOperation.SELL : ClientOperation.BUY;
    }
}
