package ru.sberbank.school.task02;

import ru.sberbank.school.task02.calculators.ExtendedCurrencyCalc;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Client implements FxClientController {

    private String beneficiary;
    private ExtendedFxConversionService reverseCalc;

    Client(ExternalQuotesService service) {
        reverseCalc = new ExtendedCurrencyCalc(service);
        beneficiary = System.getenv().get("SBRF_BENEFICIARY");

        if (beneficiary == null) {
            throw new FxConversionException("Переменная окружения \"SBRF_BENEFICIARY\" не определена");
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
    public FxResponse fetchResult(FxRequest requests) {
        Symbol symbol = getSymbol(requests);
        BigDecimal amount = getAmount(requests);
        Beneficiary beneficiary = getBeneficiary();
        ClientOperation operation = getOperation(requests);

        Optional<BigDecimal> response = reverseCalc.convertReversed(operation, symbol, amount, beneficiary);
        boolean notFound = !response.isPresent();
        String price = notFound ? null : String.valueOf(response.get());

        return new FxResponse(symbol.getSymbol(), price, String.valueOf(amount), LocalDate.now().toString(),
                operation.toString(), notFound);

    }

    private Beneficiary getBeneficiary() {
        try {
            return Beneficiary.valueOf(beneficiary.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ConverterConfigurationException("Выгодопреобретатель введен неверно", e);
        }
    }

    private BigDecimal getAmount(FxRequest request) {
        try {
            return BigDecimal.valueOf(Long.parseLong(request.getAmount()));
        } catch (NumberFormatException e) {
            throw new ConverterConfigurationException("Введен неверный объем", e);
        }
    }

    private ClientOperation getOperation(FxRequest request) {
        String requestDirection = request.getDirection().toUpperCase();

        try {
            return ClientOperation.valueOf(requestDirection);
        } catch (IllegalArgumentException e) {
            throw new ConverterConfigurationException("Введена неверная операция", e);
        }
    }

    private Symbol getSymbol(FxRequest request) {
        String requestSymbol = request.getSymbol();

        if (requestSymbol.equalsIgnoreCase(Symbol.RUB_USD.getSymbol())) {
            return Symbol.RUB_USD;
        } else if (requestSymbol.equalsIgnoreCase(Symbol.USD_RUB.getSymbol())) {
            return Symbol.USD_RUB;
        }

        throw new WrongSymbolException("Введена неверная валютная пара");
    }
}
