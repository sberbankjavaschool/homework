package ru.sberbank.school.task03;

import org.hamcrest.core.AnyOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task02.util.FxResponse;

import java.util.ArrayList;
import java.util.List;

class FormatterTest {

    private Formatter formatter = new Formatter();
    private List<FxResponse> testData = makeTestData();

    @Test
    void formatWithNullThrowsNPe() {
        Assertions.assertThrows(NullPointerException.class,
                () -> formatter.format(null));
    }

    @Test
    void formatReturnsString() {
        String string = formatter.format(testData);
        Assertions.assertNotNull(string);
        System.out.println(string);
    }

    static List<FxResponse> makeTestData() {
        List<FxResponse> data = new ArrayList<>();
        data.add(new FxResponse("USD/RUB", "54", "100", "some date", "SELL", false));
        data.add(new FxResponse("USD/RUB", "53", "1001", "some date", "SELL", false));
        data.add(new FxResponse("USD/RUB", "54", "1003", "some date", "SELL", false));
        data.add(new FxResponse("USD/RUB", "52", "1030", "some date", "BUY", false));
        data.add(new FxResponse("USD/RUB", "51", "1100", "some date", "BUY", false));
        data.add(new FxResponse("USD/RUB", "555", "4100", "some date", "BUY", false));
        data.add(new FxResponse("EUR/RUB", "5423", "1600", "some date", "SELL", false));
        data.add(new FxResponse("EUR/RUB", "1", "10120", "some date", "SELL", false));
        data.add(new FxResponse("EUR/RUB", "23", "510220", "some date", "SELL", false));
        data.add(new FxResponse("EUR/RUB", "77", "10450", "some date", "BUY", false));
        data.add(new FxResponse("USD/EUR", "23", "23100", "some date", "SELL", false));
        data.add(new FxResponse("USD/EUR", "900", "800", "some date", "BUY", false));
        data.add(new FxResponse("USD/EUR", "587", "1", "some date", "SELL", false));
        return data;
    }
}