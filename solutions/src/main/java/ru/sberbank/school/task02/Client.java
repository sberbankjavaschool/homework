package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.EmptyQuoteException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client implements FxClientController {

    private FxConversionService calculator;

    public Client(FxConversionService calculator) {

        this.calculator = calculator;
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {

        List<FxResponse> responses = new ArrayList<>();

        for (FxRequest r : requests) {
            responses.add(fetchResult(r));
        }

        return responses;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {

        Symbol symbol = getSymbol(requests.getSymbol());
        ClientOperation operation = getClientOperation(requests.getDirection());
        BigDecimal amount = getAmount(requests.getAmount());

        BigDecimal price = calculator.convert(operation, symbol, amount);

        if (price != null) {
            price = price.setScale(2, RoundingMode.HALF_UP);
            FxResponse response = new FxResponse(symbol.getSymbol(), price.toString(),
                    amount.setScale(2, RoundingMode.HALF_UP).toString(),
                    new Date().toString(), operation.toString(), false);
            return response;
        } else {
            throw new EmptyQuoteException("No quotes for specified amount!");
        }
    }

    private Symbol getSymbol(String symbolStr) {

        if (symbolStr == null) {
            throw new NullPointerException("Symbol can not be null!");
        }

        if (symbolStr.equalsIgnoreCase("USD/RUB")) {
            return Symbol.USD_RUB;
        } else {
            throw new WrongSymbolException("Wrong symbol!");
        }

    }

    private ClientOperation getClientOperation(String operationStr) {

        if (operationStr == null) {
            throw new NullPointerException("Direction can not be null!");
        }

        if (operationStr.equalsIgnoreCase("BUY")) {
            return ClientOperation.BUY;
        } else if (operationStr.equalsIgnoreCase("SELL")) {
            return ClientOperation.SELL;
        } else {
            throw  new WrongSymbolException("Wrong operation!");
        }

    }

    private BigDecimal getAmount(String amountStr) {

        if (amountStr == null) {
            throw new NullPointerException("Amount can not be null!");
        }

        try {
            return new BigDecimal(amountStr);
        } catch (NumberFormatException ex) {
            throw new WrongSymbolException("Amount should be a number!");
        }

    }
}
