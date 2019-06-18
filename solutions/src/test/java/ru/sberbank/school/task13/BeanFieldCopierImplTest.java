package ru.sberbank.school.task13;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BeanFieldCopierImplTest {
    private final BeanFieldCopier copier = new BeanFieldCopierImpl();

    @Test
    void copyProperClasses() {
        ProperTestSource source = new ProperTestSource(10, true, "test string");
        ProperTestTarget target = new ProperTestTarget();
        copier.copy(source, target);

        String expected = "10 true 'test string'";

        Assertions.assertEquals(expected, target.toString());
    }
}