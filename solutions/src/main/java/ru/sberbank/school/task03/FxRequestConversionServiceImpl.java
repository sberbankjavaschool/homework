package ru.sberbank.school.task03;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

public class FxRequestConversionServiceImpl implements FxRequestConversionService {

    public Symbol getSymbol(String symbolAsString) {

        if (symbolAsString == null) {
            throw new FxConversionException("Некорректный запрос в поле symbol");
        }

        Symbol symbol;
        switch (symbolAsString) {
            case "USD/RUB":
                symbol = Symbol.USD_RUB;
                break;
            case "RUB/USD":
                symbol = Symbol.RUB_USD;
                break;
            default:
                throw new FxConversionException("Некорректный запрос в поле symbol");
        }

        return symbol;
    }


    public ClientOperation getClientOperation(String operationAsString) {

        if (operationAsString == null) {
            throw new FxConversionException("Некорректный запрос в поле direction");
        }

        ClientOperation operation;
        switch (operationAsString) {
            case "SELL":
                operation = ClientOperation.SELL;
                break;
            case "BUY":
                operation = ClientOperation.BUY;
                break;
            default:
                throw new FxConversionException("Некорректный запрос в поле direction");
        }

        return operation;
    }


    public Beneficiary getBeneficiary() {

        String beneficiaryAsString = System.getenv("SBRF_BENEFICIARY");

        if (beneficiaryAsString == null) {
            throw new FxConversionException("Некорректный параметр SBRF_BENEFICIARY");
        }

        Beneficiary beneficiary;
        switch (beneficiaryAsString) {
            case "BANK":
                beneficiary = Beneficiary.BANK;
                break;
            case "CLIENT":
                beneficiary = Beneficiary.CLIENT;
                break;
            default:
                throw new FxConversionException("Некорректный параметр SBRF_BENEFICIARY");
        }

        return beneficiary;
    }
}
