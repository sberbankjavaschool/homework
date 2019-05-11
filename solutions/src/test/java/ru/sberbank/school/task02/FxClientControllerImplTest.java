package ru.sberbank.school.task02;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;

import static org.junit.Assert.*;

public class FxClientControllerImplTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void mainWithCorrectArgsDoesNotThrowsAnything() {
        Assertions.assertDoesNotThrow(() ->
            FxClientControllerImpl.main(new String[]{"-amount", "100", "-direction", "SELL", "-symbol", "USD/RUB"})
        );
    }

    @Test
    public void mainWithNoArgsThrowsCCe() {
        exceptionRule.expect(ConverterConfigurationException.class);
        exceptionRule.expectMessage("You should provide params symbol, direction, amount with corresponding"
                + " keys. For example: -amount 100 -direction SELL -symbol USD/RUB");
        FxClientControllerImpl.main(new String[]{});
    }

    @Test
    public void mainWithDuplicateArgsThrowsCCe() {
        exceptionRule.expect(ConverterConfigurationException.class);
        exceptionRule.expectMessage("Some of 'symbol', 'direction' or 'amount' is missing.");
        FxClientControllerImpl.main(new String[]{"-amount", "100", "-symbol", "SELL", "-symbol", "USD/RUB"});
    }

    @Test
    public void mainWithWrongFirstKeyThrowsCCe() {
        exceptionRule.expect(ConverterConfigurationException.class);
        exceptionRule.expectMessage("No key for 'a mount' value");
        FxClientControllerImpl.main(new String[]{"a mount", "100", "-symbol", "SELL", "-symbol", "USD/RUB"});
    }

    @Test
    public void mainWithWrongSecondKeyThrowsCCe() {
        exceptionRule.expect(ConverterConfigurationException.class);
        exceptionRule.expectMessage("No key for 'not a symbol' value");
        FxClientControllerImpl.main(new String[]{"-amount", "100", "not a symbol", "SELL", "-symbol", "USD/RUB"});
    }

    @Test
    public void mainWithWithTwoKeysInARowThrowsCCe() {
        exceptionRule.expect(ConverterConfigurationException.class);
        exceptionRule.expectMessage("No value for '-amount' argument");
        FxClientControllerImpl.main(new String[]{"-amount", "-amount", "-symbol", "SELL", "-symbol", "USD/RUB"});
    }
}