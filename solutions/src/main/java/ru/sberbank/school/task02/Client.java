package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Client implements FxClientController {
    private ExtendedFxConversionService calculator;
    private Beneficiary beneficiary;

    public Client(ExtendedFxConversionService calculator) {
        if (calculator == null) {
            throw new NullPointerException("ExtendedFxConversionService can't be null");
        }

        this.calculator = calculator;

        String beneficiary = System.getenv("SBRF_BENEFICIARY");

        if (beneficiary.isEmpty()) {
            throw new ConverterConfigurationException("Can't find environment variable 'SBRF_BENEFICIARY'!");
        } else if (beneficiary.equalsIgnoreCase("BANK")) {
            this.beneficiary = Beneficiary.BANK;
        } else if (beneficiary.equalsIgnoreCase("CLIENT")) {
            this.beneficiary = Beneficiary.CLIENT;
        } else {
            throw new IllegalArgumentException("'SBRF_BENEFICIARY' must be BANK or CLIENT, current: " + beneficiary);
        }
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        if (requests == null) {
            throw new NullPointerException("List of requests mustn't be null");
        }
        if (requests.isEmpty()) {
            throw new NullPointerException("List of requests mustn't be empty");
        }

        List<FxResponse> result = new ArrayList<>();

        for (FxRequest request : requests) {
            result.add(fetchResult(request));
        }
        return result;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        if (requests == null) {
            throw new NullPointerException("Request must not be null");
        }

        FxResponse response = null;
        Symbol symbol;
        ClientOperation operation;
        BigDecimal amount;

        if (requests.getSymbol().isEmpty()) {
            throw new ConverterConfigurationException("Whe haven't got Symbol");
        } else if (requests.getSymbol().equalsIgnoreCase("USD/RUB")) {
            symbol = Symbol.USD_RUB;
        } else if (requests.getSymbol().equalsIgnoreCase("RUB/USD")) {
            symbol = Symbol.RUB_USD;
        } else {
            throw new IllegalArgumentException("Symbols must be USD/RUB or RUB/USD, but current: "
                    + requests.getSymbol());
        }

        if (requests.getDirection().isEmpty()) {
            throw new ConverterConfigurationException("Whe haven't got operation");
        } else if ((requests.getDirection().equalsIgnoreCase("BUY"))) {
            operation = ClientOperation.BUY;
        } else if ((requests.getDirection().equalsIgnoreCase("SELL"))) {
            operation = ClientOperation.SELL;
        } else {
            throw new IllegalArgumentException("Operation must be BUY or SELL, but current: "
                    + requests.getDirection());
        }

        if (requests.getAmount().isEmpty()) {
            throw new ConverterConfigurationException("Whe haven't got amount");
        } else {

            try {
                DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
                decimalSymbols.setDecimalSeparator('.');
                String pattern = "##0.0#";
                DecimalFormat decimalFormat = new DecimalFormat(pattern, decimalSymbols);
                decimalFormat.setParseBigDecimal(true);
                decimalFormat.setDecimalSeparatorAlwaysShown(false);
                amount = (BigDecimal) decimalFormat.parse(requests.getAmount());
            } catch (ParseException exc) {
                throw new IllegalArgumentException("Amount hap wrong format, use xxxx.xx");
            }
        }

        Optional<BigDecimal> result = calculator.convertReversed(operation, symbol, amount, this.beneficiary);
        String price = format(result.isPresent() ? result.get().toString() : null);

        return new FxResponse(symbol.getSymbol(), price, format(amount.toString()),
                new Date().toString(), !result.isPresent());
    }

    private String format(String string) {
        if (string != null) {
            return string.indexOf('.') != -1 ? string.substring(0, string.indexOf('.') + 3) : string;
        } else {
            return null;
        }
    }
}
