package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.sberbank.school.task02.FxRequestConverter.*;

/**
 * Created by Mart
 * 10.05.2019
 **/
public class Client implements FxClientController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FxConversionService conversionService;
    private Beneficiary beneficiary;

    public Client(FxConversionService conversionService, Beneficiary beneficiary) {
        this.conversionService = conversionService;
        this.beneficiary = beneficiary;
    }

    public Client( FxConversionService conversionService) {
        this(conversionService, getBenificiary());
    }

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();
        FxConversionService fxConversionService = serviceFactory.getFxConversionService(externalQuotesService);

        Client client = new Client(fxConversionService);
        FxRequest request = getFxRequestFromArgs(args);
        FxResponse response = client.fetchResult(request);
        System.out.println(request);
        System.out.println(client.beneficiary);
        System.out.println(response);
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        Objects.requireNonNull(requests);
        List<FxResponse> resultList = new ArrayList<>();
        for (FxRequest req : requests) {
            resultList.add(fetchResult(req));
        }
        return resultList;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        Objects.requireNonNull(requests);
        BigDecimal amount = getAmount(requests);

        Optional<BigDecimal> findPrice = Optional.ofNullable(conversionService.convert(getClientOperation(requests),
                getSymbol(requests), amount));

        String dateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        boolean isNotFound = findPrice.isPresent() ? true : false;
        String foundedPrice = findPrice.isPresent()
                ? findPrice.get().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0";

        return new FxResponse(requests.getSymbol(),
                foundedPrice,
                amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                dateTime,
                requests.getDirection(),
                isNotFound);

    }

    private static FxRequest getFxRequestFromArgs(String[] args) {
        String symbol;
        String direction;
        String amount;

        if (args[0].toUpperCase().equals("USD/RUB")) {
            symbol = "USD/RUB";
        } else if (args[0].toUpperCase().equals("RUB/USD")) {
            symbol = "RUB/USD";
        } else {
            throw new WrongSymbolException("Wrong input argument.. Symbol is: "
                    + args[0] + " expected \"USD/RUB\" or \"RUB/USD\"");
        }

        if (args[1] == null) {
            throw new NullPointerException("Wrong input argument.. Client operation is: NULL"
                    + "expected \"BUY\" or \"SELL\"");
        } else if (args[1].toUpperCase().equals("BUY")) {
            direction = "BUY";
        } else if (args[1].equals("SELL")) {
            direction = "SELL";
        } else {
            throw new FxConversionException("Wrong input of Client operation.. operation is: "
                    + args[1] + " expected \"BUY\" or \"SELL\"");
        }

        if (args[2] == null) {
            throw new NullPointerException("Wrong input argument.. amount of money for operation"
                    + " is: NULL  expected some number");
        } else {
            try {
                amount = new BigDecimal(args[2]).toString();
            } catch (NumberFormatException e) {
                throw  new IllegalArgumentException("Wrong input amount of money: "
                        + args[1] + " expected some number");
            }
        }

        return new FxRequest(symbol, direction, amount);
    }

    private static Beneficiary getBenificiary() {
        return Beneficiary.valueOf(System.getenv("SBRF_BENEFICIARY"));
    }
}
