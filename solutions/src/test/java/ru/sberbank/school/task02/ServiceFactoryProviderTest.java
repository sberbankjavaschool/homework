package ru.sberbank.school.task02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServiceFactoryProviderTest {

    @Test
    void getFxConversionServiceReturnsCalculator() {
        ServiceFactory factory = new ServiceFactoryProvider();
        FxConversionService calculator = new Calculator(null);
        Assertions.assertEquals(factory.getFxConversionService(null), calculator);
    }
}