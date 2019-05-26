package ru.sberbank.school.task02.controllers;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.FxClientController;
import ru.sberbank.school.task02.enums.Symbols;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.math.RoundingMode.HALF_UP;

/**
 * Контроллер клиента для получения расчетных курсов из командной строки (args)
 * Выгодоприобретатель определяется на основе переменной окружения 'SBRF_BENEFICIARY'
 * Created by Gregory Melnikov at 03.05.2019
 */
@RequiredArgsConstructor
public class FxClientControllerImpl implements FxClientController {

    private static final int SCALE = 10;
    private static String SBRF_BENEFICIARY = System.getenv().get("SBRF_BENEFICIARY");
    private static Beneficiary BENEFICIARY;

    static {
        if (SBRF_BENEFICIARY == null) {
            SBRF_BENEFICIARY = "BANK"; //На случай, если не указана переменная окружения
        }
        BENEFICIARY = Beneficiary.valueOf(SBRF_BENEFICIARY);
        if (BENEFICIARY == null) {
            throw new IllegalArgumentException("Illegal environment beneficiary " + SBRF_BENEFICIARY);
        }
    }

    private final ExtendedFxConversionService conversionService;

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {

        List<FxResponse> responses = new ArrayList();

        for (FxRequest request : requests) {

            BigDecimal amount = new BigDecimal(request.getAmount());
            ClientOperation clientOperation = ClientOperation.valueOf(request.getDirection());
            String price = "";
            Symbol symbol = Symbols.get(request.getSymbol());
            boolean notFound = true;

            if (symbol.isCross()) {
                Optional<BigDecimal> convertResult = conversionService.convertReversed(
                        clientOperation,
                        symbol,
                        amount,
                        BENEFICIARY);

                if (convertResult.isPresent()) {
                    price = convertResult.get().setScale(SCALE, HALF_UP).stripTrailingZeros().toPlainString();
                    notFound = false;
                }
            } else {
                BigDecimal convertResult = conversionService.convert(
                        clientOperation,
                        symbol,
                        amount);

                price = convertResult.setScale(SCALE, HALF_UP).stripTrailingZeros().toPlainString();
                notFound = false;
            }
            responses.add(FxResponse.builder()
                    .symbol(request.getSymbol())
                    .price(price)
                    .amount(request.getAmount())
                    .date(LocalDateTime.now().toString())
                    .direction(request.getDirection())
                    .notFound(notFound)
                    .build());
        }
        return responses;
    }

    @Override
    public FxResponse fetchResult(FxRequest request) {
        return fetchResult(Collections.singletonList(request)).get(0);
    }
}