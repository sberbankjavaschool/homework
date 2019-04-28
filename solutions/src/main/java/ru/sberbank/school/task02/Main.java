package ru.sberbank.school.task02;

import ru.sberbank.school.task02.services.implementation.FxConversionServiceImpl;

import java.math.BigDecimal;

import static ru.sberbank.school.task02.util.ClientOperation.*;
import static ru.sberbank.school.task02.util.Symbol.*;

/**
 * Created by Gregory Melnikov at 27.04.2019
 */
public class Main {
    public static void main(String[] args) {

        double THUS = 38_000;

        BigDecimal amount = BigDecimal.valueOf(THUS);

        FxConversionService conversionService = new FxConversionServiceImpl();
//        FxConversionService conversionService = new Converter();


//        BigDecimal result = conversionService.convert(BUY, USD_RUB, amount);
        BigDecimal result = conversionService.convert(SELL, USD_RUB, amount);

        System.out.println(result.toString());
    }
}
