package ru.sberbank.school.task03;

import ru.sberbank.school.task02.util.FxResponse;
import ru.sberbank.school.task03.util.Formatter;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<FxResponse> responses = Arrays.asList(
                new FxResponse("USD/RUB", "100.00","200","16.06.19", "SELL", false),
                new FxResponse("USD/RUB", "150.00","300","16.06.19", "SELL", false),
                new FxResponse("USD/RUB", "200.00","100","16.06.19", "BUY", false),
                new FxResponse("RUB/USD", "300.00","1000","16.06.19", "BUY", false)
        );

        Formatter formatter = new Formatter();
        System.out.println(formatter.format(responses));
    }
}
