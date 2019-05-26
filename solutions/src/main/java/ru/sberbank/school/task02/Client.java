package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.ConverterConfigurationException;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.sberbank.school.task02.FxRequestConverter.*;

/**
 * Created by Mart
 * 10.05.2019
 **/
public class Client implements FxClientController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FxConversionService conversionService;
    private Beneficiary beneficiary;

    public Client(FxConversionService conversionService, Beneficiary beneficiary) {
        this.conversionService = conversionService;
        this.beneficiary = beneficiary;
    }

    public Client( FxConversionService conversionService) {
        this(conversionService, getBenificiary());
    }

    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                throw new ConverterConfigurationException("Not enough input parameters");
            }

            // declaring args from intellij "edit configuration" -> "program arguments" -> "USD/RUB BUY 1000"
            Map<String, String> params = new HashMap<>();
            params.put("symbol", args[0]);
            params.put("direction", args[1]);
            params.put("amount", args[2]);

            ServiceFactory serviceFactory = new ServiceFactoryImpl();
            ExternalQuotesService externalQuotesService = new ExternalQuotesServiceDemo();
            FxConversionService fxConversionService = serviceFactory.getFxConversionService(externalQuotesService);

            Client client = new Client(fxConversionService);
            FxRequest request = new FxRequest(params.get("symbol"), params.get("direction"), params.get("amount"));
            FxResponse response = client.fetchResult(request);
            System.out.println(request);
            System.out.println(client.beneficiary);
            System.out.println(response);
        } catch (IllegalArgumentException | FxConversionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        Objects.requireNonNull(requests);
        List<FxResponse> resultList = new ArrayList<>();
        for (FxRequest req : requests) {
            resultList.add(fetchResult(req));
        }
        return resultList;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        Objects.requireNonNull(requests);
        BigDecimal amount = getAmount(requests);

        Optional<BigDecimal> findPrice = Optional.ofNullable(conversionService.convert(getClientOperation(requests),
                getSymbol(requests), amount));

        String dateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        boolean isNotFound = !findPrice.isPresent();
        String foundedPrice = findPrice.isPresent()
                ? findPrice.get().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0";

        return new FxResponse(requests.getSymbol(),
                foundedPrice,
                amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                dateTime,
                requests.getDirection(),
                isNotFound);
    }

    private static Beneficiary getBenificiary() {
        return Beneficiary.valueOf(System.getenv("SBRF_BENEFICIARY"));
    }
}
