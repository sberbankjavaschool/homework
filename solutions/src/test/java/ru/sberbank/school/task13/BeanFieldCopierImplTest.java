package ru.sberbank.school.task13;

import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanFieldCopierImplTest {
    private static BeanFieldCopier copier;
    private static User bob;
    private static Cat cat;

    @BeforeAll
    static void init() {
        copier = new BeanFieldCopierImpl();
        bob = new User("Bob", 5, 456-34-34, "bob@mail.com");
        cat = new Cat("Mike", 3, 5, 54);
    }

    @Test
    @DisplayName("Тест на идентичность объектов после копирования")
    void justCopy() {
        User user = new User();
        copier.copy(bob, user);

        Assertions.assertEquals(bob, user);
    }

    @Test
    @DisplayName("Тест на идентичность одинаковых полей у разных объектов после копирования")
    void copyDifferentObjects() {
        copier.copy(bob, cat);

        Assertions.assertEquals(bob.getName(), cat.getName());
        Assertions.assertEquals(bob.getAge(), cat.getAge());
        Assertions.assertEquals(cat.getWeight(), 5);
    }

    @Test
    @DisplayName("Тест на идентичность одинаковых полей у разных объектов после копирования и неидентичность полей с одинаковым именем и разным типом")
    void copyDifferentObjectsWithSameNameFields() {
        copier.copy(bob, cat);

        Assertions.assertEquals(bob.getName(), cat.getName());
        Assertions.assertEquals(bob.getAge(), cat.getAge());
        Assertions.assertNotEquals(bob.getNumber(), cat.getNumber());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class User {
        private String name;
        private int age;
        private long number;
        private String mail;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class Cat {
        private String name;
        private int age;
        private long weight;
        private int number;
    }
}