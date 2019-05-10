package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Client implements FxClientController {
    private ExtendedFxConversionService converter;
    private String beneficiary;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Client(ExternalQuotesService externalQuotesService) {
        this.beneficiary = System.getenv("SBRF_BENEFICIARY");
        ServiceFactoryImpl factory = new ServiceFactoryImpl();
        this.converter = factory.getExtendedFxConversionService(externalQuotesService);
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            throw new ConverterConfigurationException("Not enough input parameters");
        }

        ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();

        Client client = new Client(externalQuotesService);

        Map<String, String> params = new HashMap<>();

        for (String arg : args) {
            params.put(arg.split("=")[0], arg.split("=")[1]);
        }

        FxRequest request = new FxRequest(params.get("symbol"), params.get("direction"), params.get("amount"));

        FxResponse response = client.fetchResult(request);

        System.out.println(response);
        System.out.println(response.getPrice());
        System.out.println(response.getDate());

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
        ClientOperation operation = getOperation(requests.getDirection());
        Symbol symbol = getSymbol(requests.getSymbol());
        BigDecimal amount = new BigDecimal(requests.getAmount());

        Optional<BigDecimal> price = beneficiary == null
                ? Optional.of(converter.convert(operation, symbol, amount))
                : converter.convertReversed(operation, symbol, amount, getBeneficiary());

        String amountStr = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

        if (!price.isPresent()) {
            return new FxResponse(symbol.getSymbol(), "0", amountStr, getDate(), operation.toString(), true);
        }

        String priceStr = price.get().setScale(2, BigDecimal.ROUND_HALF_UP).toString();

        return new FxResponse(symbol.getSymbol(), priceStr, amountStr, getDate(), operation.toString(), false);
    }

    private ClientOperation getOperation(String direction) {
        try {
            return ClientOperation.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Incorrect operation");
        }
    }

    private Symbol getSymbol(String symbolStr) {
        if (Symbol.RUB_USD.getSymbol().equalsIgnoreCase(symbolStr)) {
            return Symbol.RUB_USD;
        }
        if (Symbol.USD_RUB.getSymbol().equalsIgnoreCase(symbolStr)) {
            return Symbol.USD_RUB;
        }

        throw new WrongSymbolException("Symbol not supported");
    }

    private String getDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(formatter);
    }

    private Beneficiary getBeneficiary() {
        try {
            return Beneficiary.valueOf(beneficiary.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ConverterConfigurationException("Incorrect beneficiary");
        }
    }
}
