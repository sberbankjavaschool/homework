package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.Symbol;
import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task03.ResponseFormatter;
//import ru.sberbank.school.task03.ResponseFormatterImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {

        ServiceFactory factory = new ServiceFactoryImpl();
        ExternalQuotesService quotesService = new ExternalQuotesServiceImpl();
        FxConversionService exCalc = factory.getFxConversionService(quotesService);
        BigDecimal offer = exCalc.convert(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(10));
        System.out.println(offer);

        ExtendedFxConversionService exCalc2 = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> revOffer = exCalc2.convertReversed(ClientOperation.SELL,
                Symbol.USD_RUB, BigDecimal.valueOf(6_350_000), Beneficiary.CLIENT);
        System.out.println(revOffer);
//        List<FxRequest> requests = new ArrayList<>();
//        requests.add(new FxRequest(Symbol.USD_RUB.getSymbol(), ClientOperation.BUY.name(),
//                new BigDecimal(345_000_000).toString()));
//        requests.add(new FxRequest(Symbol.USD_RUB.getSymbol(), ClientOperation.SELL.name(),
//                new BigDecimal(91_000).toString()));
//        requests.add(new FxRequest(Symbol.USD_RUB.getSymbol(), ClientOperation.SELL.name(),
//                new BigDecimal(500).toString()));
//
//        FxClientController client = new FxClientControllerImpl(exCalc);
//
//        List<FxResponse> responses = client.fetchResult(requests);
        //ResponseFormatter rformatter = new ResponseFormatterImpl();
        //System.out.println(rformatter.format(responses));
    }
}
