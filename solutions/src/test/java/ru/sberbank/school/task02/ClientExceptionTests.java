package ru.sberbank.school.task02;

import org.junit.Before;
import org.junit.Test;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.ExternalQuotesServiceDemo;
import ru.sberbank.school.task02.util.FxRequest;


public class ClientExceptionTests {
    private FxConversionService converter;
    private FxClientController client;

    @Before
    public void before() {
        converter = new FxConverter(new ExternalQuotesServiceDemo());
        client = new FxClient(converter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSymbol() {
        FxRequest req = new FxRequest("1", "BUY", "100");
        client.fetchResult(req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDirection() {
        FxRequest req = new FxRequest("USD/RUB", "1", "100");
        client.fetchResult(req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalAmount() {
        FxRequest req = new FxRequest("USD/RUB", "BUY", "-");
        client.fetchResult(req);
    }
}
