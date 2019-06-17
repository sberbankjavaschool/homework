package ru.sberbank.school.task13;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.UndeclaredThrowableException;

class CacheProxyTest {
    private static CacheProxy proxy;
    private static IUser userProxy;

    @BeforeAll
    static void init() {
        proxy = new CacheProxy(System.getenv("directoryPath"));
        userProxy = proxy.cache(new User("Bob", 23));
    }

    @Test
    @DisplayName("Выброс исключений при создании прокси с некорректной дирректорией")
    void incorrectDirectoryPath() {
        Assertions.assertThrows(NullPointerException.class, () -> new CacheProxy(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CacheProxy("BOB"));
    }

    @Test
    @DisplayName("Выброс исключений при попытке получить прокси от объекта равным null")
    void nullService() {
        Assertions.assertThrows(NullPointerException.class, () -> proxy.cache(null));
    }

    @Test
    @DisplayName("Тест на эквивалентность объектов загруженных из файла")
    void loadFromZipFile() {
        IUser user1 = userProxy.getUserFromFile(34, "Boris");
        IUser user2 = userProxy.getUserFromFile(34, "Boris");
        Assertions.assertEquals(user1, user2);
    }

    @Test
    @DisplayName("Тест на идентичность объектов загруженных из памяти")
    void loadFromMemory() {
        IUser user1 = userProxy.getUserFromMemory(34, "Boris");
        IUser user2 = userProxy.getUserFromMemory(34, "Boris");
        Assertions.assertSame(user1, user2);
    }

}