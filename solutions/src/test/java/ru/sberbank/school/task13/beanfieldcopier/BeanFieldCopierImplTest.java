package ru.sberbank.school.task13.beanfieldcopier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.BeanFieldCopier;
import ru.sberbank.school.task13.beanfieldcopier.testclasses.AnotherPerson;
import ru.sberbank.school.task13.beanfieldcopier.testclasses.Person;
import ru.sberbank.school.task13.beanfieldcopier.testclasses.TestItem;
import ru.sberbank.school.task13.beanfieldcopier.exceptions.BeanFieldCopierException;

class BeanFieldCopierImplTest {
    private BeanFieldCopier beanFieldCopier = new BeanFieldCopierImpl();

    @Test
    @DisplayName("Тестирование копирования полей объектов одного класса")
    void copyFieldsSameClassObjectsTest() {
        Person from = new Person("Иван", "Иванов", 70, 25);
        Person to = new Person();

        beanFieldCopier.copy(from, to);

        Assertions.assertEquals(from.getName(), to.getName());
        Assertions.assertEquals(from.getSurname(), to.getSurname());
        Assertions.assertEquals(from.getWeight(), to.getWeight());
        Assertions.assertEquals(from.getAge(), to.getAge());
    }

    @Test
    @DisplayName("Тестирование копирования полей объектов разных классов, при наличии одинаковых полей")
    void copyFieldsSimilarClassObjectsTest() {
        Person from = new Person("Иван", "Иванов", 70, 25);
        AnotherPerson to = new AnotherPerson();

        beanFieldCopier.copy(from, to);

        Assertions.assertEquals(from.getName(), to.getName());
        Assertions.assertEquals(from.getWeight(), to.getWeight());
        Assertions.assertEquals(0, to.getTall());
    }

    @Test
    @DisplayName("Тестирование выбрасывания исключения при невозможности копирования")
    void exceptionWhenCantCopyTest() {
        TestItem from = new TestItem("Ivan");
        AnotherPerson to = new AnotherPerson();

        Assertions.assertThrows(BeanFieldCopierException.class, () -> beanFieldCopier.copy(from, to));

    }

}
