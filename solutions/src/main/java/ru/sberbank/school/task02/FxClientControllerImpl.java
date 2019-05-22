package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;


public class FxClientControllerImpl implements FxClientController {

    private FxConversionService converter;

    public static void main(@NonNull String[] args) {

        if (args.length != 3) {
            throw new ConverterConfigurationException("Wrong arguments amount");
        }
        FxRequest request = new FxRequest(args[0], args[1], args[2]);
//    public static void main(String[] args) {
//        FxRequest request = new FxRequest("usd/rub", "buy","1000");
        ServiceFactory factory = new ServiceFactoryImpl();
        FxConversionService exCalc = factory.getFxConversionService(new ExternalQuotesServiceImpl());
        FxClientController controller = new FxClientControllerImpl(exCalc);
        System.out.println(controller.fetchResult(request));
    }

    public FxClientControllerImpl(FxConversionService converter) {
        if (converter == null) {
            throw new FxConversionException("Converter isn't created");
        }
        this.converter = converter;
    }

    @Override
    public FxResponse fetchResult(@NonNull FxRequest request) {
        if (request.toString().equals("")) {
            throw new IllegalArgumentException("Request is empty");
        }

        ClientOperation operation = FxRequestCheck.getOperation(request);
        Symbol symbol = FxRequestCheck.getSymbol(request);
        BigDecimal amount = FxRequestCheck.getAmount(request);
        Calendar date = new GregorianCalendar();
        BigDecimal price = converter.convert(operation, symbol, amount);
        if (price == null) {
            return new FxResponse(request.getSymbol(), "", request.getAmount(), LocalDate.now().toString(),
                    request.getDirection(), true);
        } else {
            return new FxResponse(request.getSymbol(), price.setScale(2, RoundingMode.HALF_UP).toString(),
                    request.getAmount(), date.getTime().toString(), request.getDirection(), false);
        }

    }

    @Override
    public List<FxResponse> fetchResult(@NonNull List<FxRequest> requests) {
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("Empty request list");
        }
        List<FxResponse> results = new ArrayList<>();
        for (FxRequest request : requests) {
            results.add(fetchResult(request));
        }
        return results;
    }
}
