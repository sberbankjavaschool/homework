package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Client implements FxClientController {

    private String beneficiary;
    private ExtendedFxConversionService reverseCalc;

    Client(ExternalQuotesService service) {
        this.beneficiary = System.getenv("SBRF_BENEFICIARY");
        ServiceFactory factory = new ServiceFactoryImpl();
        reverseCalc = factory.getExtendedFxConversionService(service);
        if (beneficiary == null) {
            throw new FxConversionException("SBRF_BENEFICIARY не определена");
        }
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        List<FxResponse> reponses = new ArrayList<>();

        for (FxRequest request : requests) {
            reponses.add(fetchResult(request));
        }
        return reponses;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {

        ClientOperation operation = getOperation(requests);
        Symbol symbol = getSymbol(requests);
        BigDecimal amount = getAmount(requests);
        Beneficiary beneficiary = getBeneficiary();
        String date = getDate();

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return new FxResponse(
                    symbol.getSymbol(),
                    "0",
                    amount.toString(),
                    date,
                    beneficiary.toString(),
                    false);
        }

        Optional<BigDecimal> response = reverseCalc.convertReversed(operation, symbol, amount, beneficiary);

        if (!response.isPresent()) {
            new FxResponse(
                    symbol.getSymbol(),
                    "0",
                    amount.toString(),
                    date,
                    beneficiary.toString(),
                    true);
        }

        String price = response.get().setScale(2).toString();
        amount.setScale(2);

        return new FxResponse(
                symbol.getSymbol(),
                price,
                amount.toString(),
                date,
                beneficiary.toString(),
                false);
    }

    private ClientOperation getOperation(FxRequest request) {
        return ClientOperation.valueOf(request.getDirection().toUpperCase());
    }

    private BigDecimal getAmount(FxRequest request) {
        return new BigDecimal(request.getAmount());
    }

    private Symbol getSymbol(FxRequest request) {
        if (request.getSymbol().equalsIgnoreCase(Symbol.RUB_USD.getSymbol())) {
            return Symbol.RUB_USD;
        }
        if (request.getSymbol().equalsIgnoreCase(Symbol.USD_RUB.getSymbol())) {
            return Symbol.USD_RUB;
        }

        throw new WrongSymbolException("Symbol не корректно указан");
    }

    private Beneficiary getBeneficiary() {
        return Beneficiary.valueOf(beneficiary.toUpperCase());
    }

    private String getDate() {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return null;
    }

}
