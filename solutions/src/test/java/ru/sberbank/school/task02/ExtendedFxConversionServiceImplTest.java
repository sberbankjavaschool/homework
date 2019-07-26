package ru.sberbank.school.task02;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedFxConversionServiceImplTest {

    private static ExternalQuotesService quotesService;
    private static ExtendedFxConversionService converter;
    private static Symbol symbol;

    @BeforeAll
    static void init() {
        quotesService = new ExternalQuotesServiceDemo();
        converter = new ExtendedFxConversionServiceImpl(quotesService);
        symbol = Symbol.USD_RUB;

    }

    @Test
    void test1() {
        Quote q = quotesService.getQuotes(symbol).get(0);
        System.out.println(q.getOffer());
        BigDecimal amount = new BigDecimal(1000);
        System.out.println(amount.divide(q.getOffer(), 10, RoundingMode.HALF_UP));
        System.out.println("==========================================");
    }

    @Test
    void quoteVolumes() {
        quotesService.getQuotes(symbol)
                .forEach(quote -> System.out.println(quote.getVolume()
                        + " " + quote.getBid() + " " + quote.getOffer()
                        + " bid:" + BigDecimal.valueOf(1).divide(quote.getBid(),10, RoundingMode.HALF_UP)
                        + "     offer " + BigDecimal.valueOf(1).divide(quote.getOffer(),10,   RoundingMode.HALF_UP)));
    }

    @Test
    void test() {

        List<BigDecimal> amounts = new ArrayList<>();
        amounts.add(BigDecimal.valueOf(100));
        amounts.add(BigDecimal.valueOf(500));
        amounts.add(BigDecimal.valueOf(1000));
        amounts.add(BigDecimal.valueOf(1000));
        amounts.add(BigDecimal.valueOf(100000));
        amounts.add(BigDecimal.valueOf(10000000));

        Beneficiary beneficiary= Beneficiary.BANK;
        ClientOperation operation = ClientOperation.BUY;
        amounts.forEach(i -> System.out.println(
                        converter.convertReversed(operation, symbol, i, beneficiary).orElse(BigDecimal.ZERO)));
    }
}