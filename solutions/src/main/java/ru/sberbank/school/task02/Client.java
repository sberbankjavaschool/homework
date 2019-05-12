package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Client implements FxClientController {
    private ExtendedFxConversionService calculator;
    private Beneficiary beneficiary;

    public Client(ExtendedFxConversionService calculator) {
        this.calculator = Objects.requireNonNull(calculator, "ExtendedFxConversionService can't be null");

        String beneficiary = System.getenv("SBRF_BENEFICIARY");

        if (beneficiary == null || beneficiary.isEmpty()) {
            throw new ConverterConfigurationException("Can't find environment variable 'SBRF_BENEFICIARY'!");
        } else if (beneficiary.equalsIgnoreCase("BANK")) {
            this.beneficiary = Beneficiary.BANK;
        } else if (beneficiary.equalsIgnoreCase("CLIENT")) {
            this.beneficiary = Beneficiary.CLIENT;
        } else {
            throw new ConverterConfigurationException("'SBRF_BENEFICIARY' must be BANK or CLIENT, current: "
                    + beneficiary);
        }
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        Objects.requireNonNull(requests, "List of requests mustn't be null");

        if (requests.isEmpty()) {
            throw new FxConversionException("List of requests mustn't be empty");
        }

        List<FxResponse> result = new ArrayList<>();

        for (FxRequest request : requests) {
            result.add(fetchResult(request));
        }
        return result;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        Objects.requireNonNull(requests,"Request mustn't be null");
        Symbol symbol = getSymbol(requests);
        ClientOperation operation = getOperation(requests);
        BigDecimal amount = getAmount(requests);

        Optional<BigDecimal> result = calculator.convertReversed(operation, symbol, amount, this.beneficiary);
        String price = format(result.isPresent() ? result.get() : null);

        return new FxResponse(symbol.getSymbol(), price, format(amount),
                getDateAndTime(), operation.toString(), !result.isPresent());
    }

    private String getDateAndTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    private String format(BigDecimal amount) {
        DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
        decimalSymbols.setDecimalSeparator('.');

        return new DecimalFormat("0.##", decimalSymbols).format(amount);
    }

    private Symbol getSymbol(FxRequest requests) {
        if (requests.getSymbol().isEmpty()) {
            throw new FxConversionException("Whe haven't got Symbol");
        } else if (requests.getSymbol().equalsIgnoreCase("USD/RUB")) {
            return Symbol.USD_RUB;
        } else if (requests.getSymbol().equalsIgnoreCase("RUB/USD")) {
            return Symbol.RUB_USD;
        } else {
            throw new IllegalArgumentException("Symbols must be USD/RUB or RUB/USD, but current: "
                    + requests.getSymbol());
        }
    }

    private ClientOperation getOperation(FxRequest requests) {
        if (requests.getDirection().isEmpty()) {
            throw new FxConversionException("Whe haven't got operation");
        }

        try {
            return ClientOperation.valueOf(requests.getDirection().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Operation must be BUY or SELL, but current: "
                    + requests.getDirection());
        }
    }

    private BigDecimal getAmount(FxRequest requests) {
        if (requests.getAmount().isEmpty()) {
            throw new FxConversionException("Whe haven't got amount");
        } else {
            try {
                DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
                decimalSymbols.setDecimalSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#.#", decimalSymbols);
                decimalFormat.setParseBigDecimal(true);
                return (BigDecimal) decimalFormat.parse(requests.getAmount());
            } catch (ParseException exc) {
                throw new FxConversionException("Amount argument is not number");
            }
        }
    }


}
