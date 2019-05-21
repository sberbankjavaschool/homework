package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FxClientControllerImpl implements FxClientController {
    private Beneficiary beneficiary;
    private ExtendedFxConversionService calculator;

    public static void main(String[] args) {
        try {
            Map<String, String> argsMap = parseArgs(args);
            FxRequest request = new FxRequest(argsMap.get("-symbol"), argsMap.get("-direction"),
                    argsMap.get("-amount"));
            ExtendedFxConversionService calculator = new ServiceFactoryImpl()
                    .getExtendedFxConversionService(new ExternalQuotesProvider());
            FxClientController controller = new FxClientControllerImpl(calculator);
            System.out.println(controller.fetchResult(request));
        } catch (ConverterConfigurationException CCex) {
            System.out.println(CCex.getMessage());
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        if (args.length < 6) {
            throw new ConverterConfigurationException("You should provide params symbol, direction, amount with"
                    + " corresponding keys. For example: -amount 100 -direction SELL -symbol USD/RUB");
        } else {
            Map<String, String> argsMap = new HashMap<>();
            String lastKey = null;
            for (String arg : args) {
                if (arg.equals("-amount") || arg.equals("-direction") || arg.equals("-symbol")) {
                    if (lastKey != null) {
                        throw new ConverterConfigurationException("No value for '" + lastKey + "' argument");
                    } else {
                        lastKey = arg;
                    }
                } else {
                    if (lastKey == null) {
                        throw new ConverterConfigurationException("No key for '" + arg + "' value");
                    } else {
                        argsMap.put(lastKey, arg);
                        lastKey = null;
                    }
                }
            }
            if (argsMap.containsKey("-amount") && argsMap.containsKey("-direction") && argsMap.containsKey("-symbol")) {
                return argsMap;
            } else {
                throw new ConverterConfigurationException("Some of 'symbol', 'direction' or 'amount' is missing.");
            }
        }
    }

    public FxClientControllerImpl( ExtendedFxConversionService calculator) {
        this.calculator = Objects.requireNonNull(calculator, "No FxConversionService provided");
        String beneficiary = System.getenv("SBRF_BENEFICIARY");
        if (beneficiary.isEmpty()) {
            throw new ConverterConfigurationException("Environmental variable \'SBRF_BENEFICIARY\' is missing");
        }
        this.beneficiary = Beneficiary.valueOf(beneficiary);
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
    public FxResponse fetchResult(FxRequest request) {
        Symbol symbol;
        BigDecimal amount;
        FxResponse response;

        Objects.requireNonNull(request, "No request provided");
        Objects.requireNonNull(request.getSymbol(), "No symbol provided");
        Objects.requireNonNull(request.getAmount(), "No amount provided");
        Objects.requireNonNull(request.getDirection(), "No direction provided");

        if (request.getSymbol().equals("USD/RUB")) {
            symbol = Symbol.USD_RUB;
        } else if (request.getSymbol().equals("RUB/USD")) {
            symbol = Symbol.RUB_USD;
        } else {
            throw new WrongSymbolException("Wrong symbol value");
        }

        ClientOperation operation = ClientOperation.valueOf(request.getDirection());

        try {
            DecimalFormat format = new DecimalFormat();
            format.setParseBigDecimal(true);
            amount = (BigDecimal) format.parse(request.getAmount());
        } catch (ParseException ex) {
            throw new ConverterConfigurationException("Wrong amount value", ex);
        }
        Optional<BigDecimal> price = calculator.convertReversed(operation, symbol, amount, beneficiary);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (price.isPresent()) {
            response = new FxResponse(request.getSymbol(),
                    price.get().setScale(2, RoundingMode.HALF_UP).toString(),
                    request.getAmount(),
                    LocalDateTime.now().format(formatter),
                    request.getDirection(),
                    false);
        } else {
            response = new FxResponse(request.getSymbol(), "", request.getAmount(),
                    LocalDateTime.now().format(formatter), request.getDirection(), true);
        }
        return response;
    }
}
