package ru.sberbank.school.task13.cache;

import org.junit.jupiter.api.*;
import ru.sberbank.school.task13.cache.excaption.CacheException;

class CacheTest {

    private static Person person;
    private static CacheProxy proxy;
    private static IPerson personProxy;

    @BeforeAll
    static void init() {
        person = new Person("Test", 3);
        proxy = new CacheProxy(".");
        personProxy = (IPerson) proxy.cache(person);
    }

    @Test
    @DisplayName("Выброс исключения при создании прокси с некорректной дирректорией.")
    void incorrectFilesDirectory() {
        Assertions.assertThrows(NullPointerException.class, () -> new CacheProxy(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CacheProxy("?"));
    }

    @Test
    @DisplayName("Выброс исключения при передачи в прокси null.")
    void incorrectService() {
        Assertions.assertThrows(NullPointerException.class, () -> proxy.cache(null));
    }


    @Test
    @DisplayName("Проверка кеширования в FILE.")
    void cacheToFile() {
        // name = Test, oldName1 = name = Test, после вызова: name = newName = MrTest1
        String oldName1 = personProxy.rename("Test1", "Mr");
        // name = MrTest1, oldName1 != name
        String oldName2 = personProxy.rename("Test1", "Mr");
        // Два раза вызываем функцию с одинаковыми параметрами. Результат берется из файла, результаты совпадают.
        Assertions.assertEquals(oldName1, oldName2);
        // Вызываем метод с новыми параметрами.
        String oldName3 = personProxy.rename("Test2", "Mr");
        // name = MrTest1, oldName3 = name = MrTest1, после вызова: name = newName = MrTest2
        Assertions.assertEquals("MrTest1", oldName3);
    }

    @Test
    @DisplayName("Проверка кеширования в JVM.")
    void cacheToJvm() {
        Integer age1 = personProxy.getAgeAfter(10);
        person.setAge(15);
        Integer age2 = personProxy.getAgeAfter(10);
        // Два раза вызываем функцию с одинаковыми параметрами. Результат берется из JVM, результаты совпадают.
        Assertions.assertEquals(age1, age2);
        // Вызываем метод с новыми параметрами. age = 15, age3 = age + 20
        Integer age3 = personProxy.getAgeAfter(20);
        Assertions.assertEquals(Integer.valueOf(35), age3);

    }

    @Test
    @DisplayName("Проверка вызовов метода без аннотации @cache.")
    void notCache() {
        Integer age1 = personProxy.getAgeBefore(10);
        person.setAge(5);
        Integer age2 = personProxy.getAgeBefore(10);
        // Вызываем методы с одинаковыми параметрами, результаты выполнения разные.
        Assertions.assertNotEquals(age1, age2);
    }

    @Test
    @DisplayName("Вызов метода void с аннотацией @cache.")
    void setCache() {
        Assertions.assertThrows(CacheException.class, () -> personProxy.setName("TestTest"));
    }

    @Test
    @DisplayName("Вызов метода без параметров с аннотацией @cache.")
    void getCache() {
        Assertions.assertThrows(CacheException.class, () -> personProxy.getName());
    }

}
