package ru.sberbank.school.task02.service;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxClientController;
import ru.sberbank.school.task02.ServiceFactory;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Client implements FxClientController {

    private String beneficiary;
    private ExtendedFxConversionService reverseCalculator;

    public Client(ExternalQuotesService service) {
        this.beneficiary = System.getenv("SBRF_BENEFICIARY");
        ServiceFactory factory = new ServiceFactoryImpl();
        this.reverseCalculator = factory.getExtendedFxConversionService(service);
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        List<FxResponse> list = new ArrayList<>();

        for (FxRequest fr : requests) {
            list.add(fetchResult(fr));
        }

        return list;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        Optional<BigDecimal> response;

        try {
            response = reverseCalculator
                    .convertReversed(getOperation(requests),
                            getSymbol(requests),
                            getAmount(requests),
                            getBeneficiary());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new FxConversionException("Сan't calculate the response", e);
        }

        if (response.isEmpty()) {
            throw new FxConversionException("Response is not found, got a space.");
        }

        String price = response.get().setScale(2, RoundingMode.HALF_DOWN).toString();
        BigDecimal amount = getAmount(requests).setScale(2, RoundingMode.HALF_DOWN);

        return new FxResponse(getSymbol(requests).toString(),
                price,
                amount.toString(),
                getDate(),
                getBeneficiary().toString(),
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

        throw new WrongSymbolException("Incorrectly specified a symbol");
    }

    private Beneficiary getBeneficiary() {
        return Beneficiary.valueOf(beneficiary.toUpperCase());
    }

    private String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
