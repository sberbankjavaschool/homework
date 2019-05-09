package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FxClientControllerImpl implements FxClientController {
    private Beneficiary beneficiary;
    private ExtendedFxConversionService calculator;

    public static void main(String[] args) {
        ExtendedFxConversionService calculator = new ServiceFactoryImpl()
                .getExtendedFxConversionService(new ExternalQuotesProvider());
        FxClientController controller = new FxClientControllerImpl(calculator);
        if (args.length != 3) {
            throw new ConverterConfigurationException("You should provide exactly 3 params: symbol, direction, amount");
        }
        FxRequest request = new FxRequest(args[0], args[1], args[2]);
        System.out.println(controller.fetchResult(request));
    }

    public FxClientControllerImpl(ExtendedFxConversionService calculator) {
        if (calculator == null) {
            throw new NullPointerException("No FxConversionService provided");
        }
        this.calculator = calculator;
        String beneficiary = System.getenv("SBRF_BENEFICIARY");
        if (beneficiary.isEmpty()) {
            throw new ConverterConfigurationException("Environmental variable \'SBRF_BENEFICIARY\' is missing");
        }
        if (beneficiary.equals("BANK")) {
            this.beneficiary = Beneficiary.BANK;
        } else if (beneficiary.equals("CLIENT")) {
            this.beneficiary = Beneficiary.CLIENT;
        } else {
            throw new ConverterConfigurationException("Wring \'SBRF_BENEFICIARY\' value");
        }
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
        ClientOperation operation;
        FxResponse response;

        if (request == null) {
            throw new NullPointerException("No request provided");
        }
        if (request.getSymbol() == null) {
            throw new NullPointerException("No symbol provided");
        }
        if (request.getAmount() == null) {
            throw new NullPointerException("No amount provided");
        }
        if (request.getDirection() == null) {
            throw new NullPointerException("No direction provided");
        }

        if (request.getSymbol().equals("USD/RUB")) {
            symbol = Symbol.USD_RUB;
        } else if (request.getSymbol().equals("RUB/USD")) {
            symbol = Symbol.RUB_USD;
        } else {
            throw new WrongSymbolException("Wrong symbol value");
        }

        if (request.getDirection().equals("BUY")) {
            operation = ClientOperation.BUY;
        } else if (request.getDirection().equals("SELL")) {
            operation = ClientOperation.SELL;
        } else {
            throw new ConverterConfigurationException("Wrong direction value");
        }
        try {
            DecimalFormat format = new DecimalFormat();
            format.setParseBigDecimal(true);
            amount = (BigDecimal) format.parse(request.getAmount());
        } catch (ParseException ex) {
            throw new ConverterConfigurationException("Wrong amount value", ex);
        }
        Optional<BigDecimal> price = calculator.convertReversed(operation, symbol, amount, beneficiary);
        if (price.isPresent()) {
            response = new FxResponse(request.getSymbol(),
                    price.get().setScale(2, RoundingMode.HALF_UP).toString(),
                    request.getAmount(),
                    new Date().toString(),
                    false);
        } else {
            response = new FxResponse(request.getSymbol(), "", request.getAmount(),
                    new Date().toString(), true);
        }
        return response;
    }
}
