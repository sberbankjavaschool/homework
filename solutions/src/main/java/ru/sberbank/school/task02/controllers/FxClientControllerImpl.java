package ru.sberbank.school.task02.controllers;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.FxClientController;
import ru.sberbank.school.task02.ServiceFactory;
import ru.sberbank.school.task02.enums.Symbols;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Контроллер клиента для получения расчетных курсов из командной строки (args)
 * Выгодоприобретатель определяется на основе переменной окружения 'SBRF_BENEFICIARY'
 * Created by Gregory Melnikov at 03.05.2019
 */
@RequiredArgsConstructor
public class FxClientControllerImpl implements FxClientController {

    private static final Map<String, String> environment = System.getenv();
    private static final String SBRF_BENEFICIARY = environment.get("SBRF_BENEFICIARY");
    private static final Beneficiary BENEFICIARY = Beneficiary.valueOf(SBRF_BENEFICIARY);
    private static final int SCALE = 2;

    private final ExternalQuotesService externalQuotesService;

    private final ServiceFactory serviceFactory;

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        ExtendedFxConversionService conversionService =
                serviceFactory.getExtendedFxConversionService(externalQuotesService);

        List<FxResponse> responses = new ArrayList();

        ClientOperation clientOperation;
        BigDecimal amount;

        for (FxRequest request : requests) {
            clientOperation = ClientOperation.valueOf(request.getDirection());
            amount = new BigDecimal(request.getAmount());

            for (Symbols symbolsElement : Symbols.values()) {
                String price = "";
                boolean notFound = true;
                boolean requestAndElementSymbolsAreSame = symbolsElement.getKey().equals(request.getSymbol());

                if (requestAndElementSymbolsAreSame && !symbolsElement.isCross()) {
                    BigDecimal convertResult = conversionService.convert(
                            clientOperation,
                            symbolsElement.getSymbol(),
                            amount);

                    price = convertResult.setScale(SCALE, BigDecimal.ROUND_HALF_EVEN).toString();
                    notFound = false;

                } else if (requestAndElementSymbolsAreSame && symbolsElement.isCross()) {
                    Optional<BigDecimal> convertResult = conversionService.convertReversed(
                            clientOperation,
                            symbolsElement.getSymbol(),
                            amount,
                            BENEFICIARY);

                    if (convertResult.isPresent()) {
                        price = convertResult.get().setScale(SCALE, BigDecimal.ROUND_HALF_EVEN).toString();
                        notFound = false;
                    }
                }
                if (requestAndElementSymbolsAreSame) {
                    responses.add(FxResponse.builder()
                            .symbol(request.getSymbol())
                            .price(price)
                            .amount(request.getAmount())
                            .notFound(notFound)
                            .build());
                }
            }
        }
        return responses;
    }

    @Override
    public FxResponse fetchResult(FxRequest request) {

        List<FxRequest> requests = new ArrayList();

        requests.add(request);

        return fetchResult(requests).get(0);
    }
}
