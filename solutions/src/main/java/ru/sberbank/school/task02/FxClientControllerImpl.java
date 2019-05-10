package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class FxClientControllerImpl implements FxClientController {

    private FxConversionService converter;

    public static void main(@NonNull String[] args) {
    //public static void main(String[] args) {
        // FxRequest request = new FxRequest("usd/rub", "buy", "1000");
        if (args.length != 3) {
            throw new ConverterConfigurationException("Wrong arguments amount");
        }
        FxRequest request = new FxRequest(args[0], args[1], args[2]);
        ServiceFactory factory = new ServiceFactoryImpl();
        FxConversionService exCalc = factory.getFxConversionService(new ExternalQuotesServiceImpl());
        FxClientController controller = new FxClientControllerImpl(exCalc);
        System.out.println(controller.fetchResult(request));
    }

    public FxClientControllerImpl (FxConversionService converter) {
        if (converter == null) {
            throw new NullPointerException("Converter isn't created");
        }
        this.converter = converter;
    }

    @Override
    public FxResponse fetchResult(@NonNull FxRequest request) {
        ClientOperation operation;
        Symbol symbol;
        BigDecimal amount;

        if (request.getDirection() == null) {
            throw new ConverterConfigurationException("Operation is null");
        } else if (FxRequestCheck.getOperation(request).equals(ClientOperation.BUY)) {
            operation = ClientOperation.BUY;
        } else if (FxRequestCheck.getOperation(request).equals(ClientOperation.SELL)) {
            operation = ClientOperation.SELL;
        } else {
            throw new ConverterConfigurationException("Wrong operation");
        }

        if (request.getSymbol() == null) {
            throw new ConverterConfigurationException("Symbol is null");
        } else if (FxRequestCheck.getSymbol(request).equals(Symbol.USD_RUB)) {
            symbol = Symbol.USD_RUB;
        } else if (FxRequestCheck.getSymbol(request).equals(Symbol.RUB_USD)) {
            symbol = Symbol.RUB_USD;
        } else {
            throw new WrongSymbolException("Wrong symbol");
        }

        if (request.getAmount() == null) {
            throw new ConverterConfigurationException("Amount is null");
        }
        try {
            amount = BigDecimal.valueOf(Double.valueOf(request.getAmount()));
        } catch (NumberFormatException ex) {
            throw new ConverterConfigurationException("Wrong amount value");
        }

        BigDecimal price = converter.convert(operation, symbol, amount);
        if (price == null) {
            return new FxResponse(request.getSymbol(), "", request.getAmount(), LocalDate.now().toString(),
                    request.getDirection(), true);
        } else {
            return new FxResponse(request.getSymbol(), price.setScale(2, RoundingMode.HALF_UP).toString(),
                    request.getAmount(), LocalDate.now().toString(), request.getDirection(), false);
        }

    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        List<FxResponse> results = new ArrayList<>();
        for (FxRequest request : requests) {
            results.add(fetchResult(request));
        }
        return results;
    }
}
