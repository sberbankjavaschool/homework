package ru.sberbank.school.task13.cacheproxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.cacheproxy.util.TestClass;

import java.io.File;
import java.util.concurrent.TimeUnit;

class CacheProxyTest {

    @Test
    void testMemoryCache() throws InterruptedException {
        CacheProxy proxy = new CacheProxy("./src/test/java/ru/sberbank/school/task13/cacheproxy/cachefiles");
        TestClass proxyInstance = (TestClass) proxy.cache(new TestClass());
        String resultFirstCall = proxyInstance.getResultUseMemoryCache();
        TimeUnit.SECONDS.sleep(1);
        String resultSecondCall = proxyInstance.getResultUseMemoryCache();
        Assertions.assertEquals(resultFirstCall, resultSecondCall);
    }

    @Test
    void testFileCache() {
        CacheProxy proxy = new CacheProxy("./src/test/java/ru/sberbank/school/task13/cacheproxy/cachefiles");
        TestClass proxyInstance = (TestClass) proxy.cache(new TestClass());
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

