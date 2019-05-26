package ru.sberbank.school.task02;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

class ReverseCalculatorTest {
    private ServiceFactory factory = new ServiceFactoryImpl();
    private ExternalQuotesService quotesService = new ExternalQuotesProvider();

    @Test
    void convertReversedNoDeltaSell() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                BigDecimal.valueOf(1000 - 1), Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.01).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    void convertReversedNoDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(1000), Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.001).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    void convertReversedZeroDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(1000), 0, Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.001).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    void convertReversedDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(50_000), 20_000, Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.0005).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    void convertReversedHugeDeltaBuy() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                BigDecimal.valueOf(275_000), 150_000, Beneficiary.CLIENT);
        BigDecimal offer = null;
        if (optionalOffer.isPresent()) {
            offer = optionalOffer.get();
        }
        Assertions.assertEquals(BigDecimal.valueOf(0.00025).setScale(10, RoundingMode.HALF_UP), offer);
    }

    @Test
    void convertReversedNullOperationThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculator.convertReversed(null, Symbol.USD_RUB, BigDecimal.valueOf(1), Beneficiary.CLIENT);
        }, "No operation provided");
    }

    @Test
    void convertReversedNullSymbolThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculator.convertReversed(ClientOperation.BUY, null, BigDecimal.valueOf(1), Beneficiary.CLIENT);
        }, "No symbol provided");
    }

    @Test
    void convertReversedNullAmountThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, null, Beneficiary.CLIENT);
        }, "No amount provided");
    }

    @Test
    void convertReversedZeroAmountThrowsIAe() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.ZERO, Beneficiary.CLIENT);
        }, "Wrong amount");
    }

    @Test
    void convertReversedNegativeAmountThrowsIAe() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(-1), Beneficiary.CLIENT);
        }, "Wrong amount");
    }

    @Test
    void convertReversedNegativeDeltaThrowsIAe() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.TEN, -1, Beneficiary.CLIENT);
        }, "Negative delta is mindless");
    }

    @Test
    void convertReversedNullBeneficiaryThrowsNPeWithSpecificMessage() {
        ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB, BigDecimal.valueOf(1), null);
        }, "No beneficiary provided");
    }

    @Nested
    class TestsWithDifferentQuoteService {

        private ExternalQuotesService quotesService = new ExternalQuotesProviderWithOverlapAndHole();

        @Test
        void convertReversedNoDeltaSell() {
            ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
            Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                    BigDecimal.valueOf(293750 - 1), Beneficiary.CLIENT);
            BigDecimal offer = null;
            if (optionalOffer.isPresent()) {
                offer = optionalOffer.get();
            }
            Assertions.assertEquals(BigDecimal.valueOf(0.02).setScale(10, RoundingMode.HALF_UP), offer);
        }

        @Test
        void convertReversedNoDeltaBuy() {
            ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
            Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                    BigDecimal.valueOf(277154136 - 1), Beneficiary.CLIENT);
            BigDecimal offer = null;
            if (optionalOffer.isPresent()) {
                offer = optionalOffer.get();
            }
            Assertions.assertEquals(BigDecimal.valueOf(0.0178571429).setScale(10, RoundingMode.HALF_UP), offer);
        }

        @Test
        void convertReversedNoDeltaSellWithOverlayClientWin() {
            ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
            Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                    BigDecimal.valueOf(267255774 - 1), Beneficiary.CLIENT);
            BigDecimal offer = null;
            if (optionalOffer.isPresent()) {
                offer = optionalOffer.get();
            }
            Assertions.assertEquals(BigDecimal.valueOf(0.0185185185).setScale(10, RoundingMode.HALF_UP), offer);
        }

        @Test
        void convertReversedNoDeltaBuyWithOverlayClientWin() {
            ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
            Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.BUY, Symbol.USD_RUB,
                    BigDecimal.valueOf(6889762 - 1), Beneficiary.CLIENT);
            BigDecimal offer = null;
            if (optionalOffer.isPresent()) {
                offer = optionalOffer.get();
            }
            Assertions.assertEquals(BigDecimal.valueOf(0.0178571429).setScale(10, RoundingMode.HALF_UP), offer);
        }

        @Test
        void convertReversedNoDeltaSellWithHole() {
            ExtendedFxConversionService calculator = factory.getExtendedFxConversionService(quotesService);
            Optional<BigDecimal> optionalOffer = calculator.convertReversed(ClientOperation.SELL, Symbol.USD_RUB,
                    BigDecimal.valueOf(293750), Beneficiary.CLIENT);
            BigDecimal offer = null;
            if (optionalOffer.isPresent()) {
                offer = optionalOffer.get();
            }
            Assertions.assertNull(offer);
        }
    }
}