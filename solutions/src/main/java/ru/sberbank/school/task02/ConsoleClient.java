package ru.sberbank.school.task02;

import org.apache.commons.cli.*;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.util.Symbol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConsoleClient implements FxClientController {
    private CommandLineParser cmdParser;
    private Options cmdOptions;
    private FxConversionService converter;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        if (requests.size() <= 0) {
            return null;
        }
        List<FxResponse> result = new ArrayList<>();
        for (FxRequest request : requests) {
            result.add(fetchResult(request));
        }
        return result;
    }

    public ConsoleClient(ExternalQuotesService service) {
        converter = new Converter(service);
        cmdParser = new DefaultParser();
        cmdOptions = new Options();
        cmdOptions.addOption("o", "operation", true, "ClientOperation");
        cmdOptions.addOption("s", "symbol", true, "Symbol");
        cmdOptions.addOption("a", "amount", true, "Amount");
    }

    @Override
    public FxResponse fetchResult(FxRequest request) {
        Symbol symbol = getSymbol(request.getSymbol());
        ClientOperation operation = getOperation(request.getDirection());
        BigDecimal amount = getAmount(request.getAmount());
        if (symbol == null || amount == null || operation == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return makeResponse(request,null);
        }
        BigDecimal result = converter.convert(operation,symbol,amount);
        if (result == null) {
            return makeResponse(request,null);
        }
        return makeResponse(request,result);
    }

    private FxResponse makeResponse(FxRequest request, BigDecimal result) {
        LocalDateTime date = LocalDateTime.now();
        if (result == null) {
            return new FxResponse(request.getSymbol(),"",
                                  request.getAmount(),date.format(formatter),
                                  request.getDirection(),true);
        }

        BigDecimal amount = new BigDecimal(request.getAmount()).setScale(2, RoundingMode.HALF_UP);
        result = result.setScale(2,RoundingMode.HALF_UP);

        return new FxResponse(request.getSymbol(),result.toString(),
                              amount.toString(),date.format(formatter),
                              request.getDirection(),false);
    }

    public FxRequest makeRequest(String[] args) {
        CommandLine cmdLine;
        try {
            cmdLine = cmdParser.parse(cmdOptions, args);
        } catch (ParseException e) {
            return new FxRequest(null, null, null);
        }

        String symbol = cmdLine.getOptionValue("s");
        String operation = cmdLine.getOptionValue("o");
        String amount = cmdLine.getOptionValue("a");

        if (symbol != null) {
            symbol = symbol.toUpperCase();
        }
        if (operation != null) {
            operation = operation.toUpperCase();
        }

        return new FxRequest(symbol, operation, amount);
    }

    private Symbol getSymbol(String symbol) {
        if (symbol == null) {
            return null;
        }
        symbol = symbol.replace("/","_");
        Class symbolClass = Symbol.class;
        Field[] fields = symbolClass.getFields();
        for (Field field : fields) {
            int filedMods = field.getModifiers();
            if (field.getName().equals(symbol) && Modifier.isStatic(filedMods)) {
                try {
                    return (Symbol) field.get(symbolClass);
                } catch (IllegalAccessException ignored) {
                    continue;
                }
            }
        }
        return null;
    }

    private ClientOperation getOperation(String operation) {
        if (operation == null) {
            return null;
        }
        return operation.equals("BUY") ? ClientOperation.BUY : ClientOperation.SELL;
    }

    private BigDecimal getAmount(String amount) {
        if (amount == null) {
            return null;
        }
        return new BigDecimal(amount);
    }
}
