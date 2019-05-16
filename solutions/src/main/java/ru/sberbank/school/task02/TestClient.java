package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.util.*;
import ru.sberbank.school.task03.Formatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestClient {

    public static void main(String[] args) {
        try {
            ExternalQuotesService service = new ExternalQuotesServiceDemo();
            Client client = new Client(service);
            List<FxRequest> requests = new ArrayList<>();
            requests.add(new FxRequest(args[0], args[1], args[2]));
            requests.add(new FxRequest(Symbol.USD_RUB.getSymbol(), ClientOperation.BUY.name(),
                    new BigDecimal(79_000_000).toString()));
            requests.add(new FxRequest(Symbol.USD_RUB.getSymbol(), ClientOperation.SELL.name(),
                    new BigDecimal(99_000).toString()));

            List<FxResponse> responses = client.fetchResult(requests);
            Formatter formatter = new Formatter();
            System.out.println(formatter.format(responses));
//            if (!response.isNotFound()) {
//                System.out.println(response.toString());
//            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FxConversionException("В аргументы были переданы не все требуемые поля", e);
        }

    }

}
