package ru.sberbank.school.task02;

import org.junit.Assert;
import org.junit.Test;

public class ServiceFactoryProviderTest {

    @Test
    public void getFxConversionServiceReturnsCalculator() {
        ServiceFactory factory = new ServiceFactoryProvider();
        FxConversionService calculator = new Calculator(null);
        Assert.assertEquals(factory.getFxConversionService(null), calculator);
    }
}