package ru.sberbank.school.task13.beanfieldcopier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.beanfieldcopier.exception.BeanFieldCopierException;
import ru.sberbank.school.task13.beanfieldcopier.model.IntData;
import ru.sberbank.school.task13.beanfieldcopier.model.StringData;

public class BeanFieldCopierTest {

    @Test
    public void copyTest() {
        IntData from = new IntData(10);
        IntData to = new IntData(0);
        BeanFieldCopierImpl beanFieldCopier = new BeanFieldCopierImpl();
        beanFieldCopier.copy(from, to);
        Assertions.assertEquals(from.getValue(), to.getValue());
    }

    @Test
    public void testCopyIrreducibleTypes() {
        IntData from = new IntData(10);
        StringData to = new StringData("0");
        BeanFieldCopierImpl beanFieldCopier = new BeanFieldCopierImpl();
        Assertions.assertThrows(BeanFieldCopierException.class, () -> beanFieldCopier.copy(from, to));
    }
}
