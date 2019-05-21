package ru.sberbank.school.task02;

import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import ru.sberbank.school.task02.exception.ConverterConfigurationException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FxClientControllerImplTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restore() {
        System.setOut(originalOut);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Ignore("Fail on build, because of no SBRF_BENEFICIARY env var")
    @Test
    public void mainWithCorrectArgsDoesNotThrowsAnything() {
        Assertions.assertDoesNotThrow(() ->
                FxClientControllerImpl.main(new String[]{"-amount", "100", "-direction", "SELL", "-symbol", "USD/RUB"})
        );
    }

    @Test
    public void mainWithNoArgsThrowsCCe() {
        String expect = "You should provide params symbol, direction, amount with corresponding"
                + " keys. For example: -amount 100 -direction SELL -symbol USD/RUB";
        FxClientControllerImpl.main(new String[]{});
        Assertions.assertEquals(expect, outContent.toString().trim());
    }

    @Test
    public void mainWithDuplicateArgsThrowsCCe() {
        String expect = "Some of 'symbol', 'direction' or 'amount' is missing.";
        FxClientControllerImpl.main(new String[]{"-amount", "100", "-symbol", "SELL", "-symbol", "USD/RUB"});
        Assertions.assertEquals(expect, outContent.toString().trim());
    }

    @Test
    public void mainWithWrongFirstKeyThrowsCCe() {
        String expect = "No key for 'a mount' value";
        FxClientControllerImpl.main(new String[]{"a mount", "100", "-symbol", "SELL", "-symbol", "USD/RUB"});
        Assertions.assertEquals(expect, outContent.toString().trim());
    }

    @Test
    public void mainWithWrongSecondKeyThrowsCCe() {
        String expect = "No key for 'not a symbol' value";
        FxClientControllerImpl.main(new String[]{"-amount", "100", "not a symbol", "SELL", "-symbol", "USD/RUB"});
        Assertions.assertEquals(expect, outContent.toString().trim());
    }

    @Test
    public void mainWithWithTwoKeysInARowThrowsCCe() {
        String expect = "No value for '-amount' argument";
        FxClientControllerImpl.main(new String[]{"-amount", "-amount", "-symbol", "SELL", "-symbol", "USD/RUB"});
        Assertions.assertEquals(expect, outContent.toString().trim());
    }
}