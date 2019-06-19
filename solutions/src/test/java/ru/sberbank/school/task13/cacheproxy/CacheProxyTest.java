package ru.sberbank.school.task13.cacheproxy;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.cacheproxy.exception.CachedException;
import ru.sberbank.school.task13.cacheproxy.util.TestInterface;
import ru.sberbank.school.task13.cacheproxy.util.TestInterfaceImpl;

import java.io.File;

class CacheProxyTest {

    @Test
    void testMemoryCache() {
        CacheProxy proxy = new CacheProxy("./src/test/java/ru/sberbank/school/task13/cacheproxy/cachefiles");
        TestInterface proxyInstance = (TestInterface) proxy.cache(new TestInterfaceImpl());
        String resultFirstCall = proxyInstance.getResultUseMemoryCache();
        String resultSecondCall = proxyInstance.getResultUseMemoryCache();
        Assertions.assertEquals(resultFirstCall, resultSecondCall);
    }

    @Test
    void testFileCache() {
        CacheProxy proxy = new CacheProxy("./src/test/java/ru/sberbank/school/task13/cacheproxy/cachefiles");
        TestInterface proxyInstance = (TestInterface) proxy.cache(new TestInterfaceImpl());
        String resultFirstCall = proxyInstance.getResultUseFileCache("str", 3.14, 42);
        String resultSecondCall = proxyInstance.getResultUseFileCache("str", 3.14, 0);
        Assertions.assertEquals(resultFirstCall, resultSecondCall);
        File directory = new File(proxy.getDirectory());
        for (File file : directory.listFiles()) {
            file.delete();
        }
    }

    @Test
    void wrongPath() {
        Assertions.assertThrows(NullPointerException.class, () -> new CacheProxy(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CacheProxy("wrong path"));
    }

    @Test
    void nullService() {
        CacheProxy proxy = new CacheProxy(".");
        Assertions.assertThrows(NullPointerException.class, () -> proxy.cache(null));
    }

}

