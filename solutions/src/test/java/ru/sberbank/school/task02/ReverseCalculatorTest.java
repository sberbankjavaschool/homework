package ru.sberbank.school.task02;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ReverseCalculatorTest {
    private ServiceFactory factory = new ServiceFactoryImpl();
    private ExternalQuotesService quotesService = new ExternalQuotesProvider();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void convertReversedNoDeltaSell() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                BigDecimal.valueOf(1000), Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.0001111111).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    public void convertReversedNoDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(1000), Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.005).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    public void convertReversedZeroDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(1000), 0, Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.005).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Ignore("Don't implemented (yet)")
    @Test
    public void convertReversedDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(81_000), 1, Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.001).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    public void convertReversedNullOperationThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("No operation provided");
        calculator.convertReversed(null, Symbol.USD_RUB, BigDecimal.valueOf(1), Beneficiary.CLIENT);
    }

    @Test
    public void convertReversedNullSymbolThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("No symbol provided");
        calculator.convertReversed(ClientOperation.BUY, null, BigDecimal.valueOf(1), Beneficiary.CLIENT);
    }

    @Test
    public void convertReversedNullAmountThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("No amount provided");
        calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, null, Beneficiary.CLIENT);
    }

    @Test
    public void convertReversedZeroAmountThrowsIAe() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Wrong amount");
        calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.ZERO, Beneficiary.CLIENT);
    }

    @Test
    public void convertReversedNegativeAmountThrowsIAe() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Wrong amount");
        calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(-1), Beneficiary.CLIENT);
    }

    @Test
    public void convertReversedNegativeDeltaThrowsIAe() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Negative delta is mindless");
        calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.TEN, -1, Beneficiary.CLIENT);
    }

    @Test
    public void convertReversedNullBeneficiaryThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("No beneficiary provided");
        calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1), null);
    }
}