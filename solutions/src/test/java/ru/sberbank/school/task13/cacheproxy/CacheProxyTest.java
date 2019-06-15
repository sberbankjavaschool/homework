package ru.sberbank.school.task13.cacheproxy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.cacheproxy.cache.CacheProxy;
import ru.sberbank.school.task13.cacheproxy.exception.CacheProxyException;
import ru.sberbank.school.task13.cacheproxy.services.CacheTest;
import ru.sberbank.school.task13.cacheproxy.services.CacheTestImpl;
import ru.sberbank.school.task13.cacheproxy.utils.CacheProxyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CacheProxyTest {
    private static String dir;
    private static final CacheProxy cacheProxyImpl = new CacheProxy("");

    @BeforeAll
    public static void init() throws IOException {
        dir = String.valueOf(Files.createTempDirectory(""));
    }

    @Test
    public void testMemoryCache() {
        CacheTest proxyInstance = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("MUR"));
        String resultFirstCall = proxyInstance.getResultUseMemoryCache();
        String resultSecondCall = proxyInstance.getResultUseMemoryCache();
        Assertions.assertEquals(resultFirstCall, resultSecondCall);
        Assertions.assertEquals(1, proxyInstance.getCallCounter());
    }

    @Test
    public void testFileCache() throws CacheProxyException {
        CacheTest proxyInstance = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("MUR"));
        String resultFirstCall = proxyInstance.getResultUseFileCache();
        String resultSecondCall = proxyInstance.getResultUseFileCache();
        Assertions.assertEquals(resultFirstCall, resultSecondCall);
        Assertions.assertEquals(1, proxyInstance.getCallCounter());
        String key = CacheProxyUtils.getKey(new Class[] {}, "cache_type", "getResultUseFileCache", null);
        File file = new File(dir + File.separator + key);
        Assertions.assertTrue(file.exists());
        file.deleteOnExit();
    }

    @Test
    public void testZipFile() throws CacheProxyException {
        CacheTest proxyInstance = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("MUR"));
        proxyInstance.zipFileTest();
        String key = CacheProxyUtils.getKey(new Class[] {}, "", "zipFileTest", null);
        File file = new File(dir + File.separator + key + ".zip");
        Assertions.assertTrue(file.exists());
        file.deleteOnExit();
    }

    @Test
    public void testCutList() {
        CacheTest proxyInstance = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("MUR"));
        List result = proxyInstance.createList(500);
        Assertions.assertEquals(100, result.size());
    }

    @Test
    public void testKeyGenerationWhenSpecifiedArguments() {
        CacheTest proxyInstance = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("MUR"));
        proxyInstance.stringSize("STR", 100);
        proxyInstance.stringSize("STR", 999);
        Assertions.assertEquals(1, proxyInstance.getCallCounter());
    }

    @AfterAll
    public static void finish() {
        new File(dir).deleteOnExit();
    }
}
