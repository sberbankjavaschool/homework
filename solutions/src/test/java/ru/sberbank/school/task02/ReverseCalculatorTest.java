package ru.sberbank.school.task02;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ReverseCalculatorTest {
    private ServiceFactory factory = new ServiceFactoryImpl();
    private ExternalQuotesService quotesService = new ExternalQuotesProvider();

    @Test
    public void convertReversedNoDelta() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                BigDecimal.valueOf(1000), Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.001).setScale(10, RoundingMode.HALF_UP), offer);
    }
}