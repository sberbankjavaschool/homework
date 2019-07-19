package ru.sberbank.school.task13.cacheProxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.cacheProxy.annotation.Cache;
import ru.sberbank.school.task13.cacheProxy.cache.CacheProxy;
import ru.sberbank.school.task13.cacheProxy.cache.HandlerProxy;
import ru.sberbank.school.task13.cacheProxy.exception.CacheProxyException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class cacheProxyTest {


    private static final CacheProxy cacheProxyImpl = new CacheProxy("D:\\");

    @BeforeAll
    public static void init() {

    }

    @Test
    public void testCacheJVM() {
        CacheTest cacheProxy = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("qwe"));
        String s1 = cacheProxy.cacheInJVM();
        String s2 = cacheProxy.cacheInJVM();
        Assertions.assertEquals(s1, s2);
        Assertions.assertEquals(1, cacheProxy.getCount());
    }

    @Test
    public void testFileCache() {
        CacheTest cacheProxy = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("qwe"));
        String s1 = cacheProxy.cacheInFile();
        String s2 = cacheProxy.cacheInFile();
        Assertions.assertEquals(s1, s2);
        Assertions.assertEquals(1, cacheProxy.getCount());
        Cache annotation = null;
        Method method = null;
        try {
            method = CacheTest.class.
                    getDeclaredMethod("cacheInFile", null);
            annotation = method.getDeclaredAnnotation(Cache.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String key = HandlerProxy.getKey(annotation, method, null);
        File file = new File("D:\\" + File.separator + key);
        Assertions.assertTrue(file.exists());
    }

    @Test
    public void testCacheZip() {
        CacheTest cacheProxy = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("qwe"));
        cacheProxy.cacheInZip();
        Cache annotation = null;
        Method method = null;
        try {
            method = CacheTest.class.
                    getDeclaredMethod("cacheInZip", null);
            annotation = method.getDeclaredAnnotation(Cache.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String key = HandlerProxy.getKey(annotation, method, null);
        File file = new File("D:\\" + File.separator + key + ".zip");
        Assertions.assertTrue(file.exists());


    }

    @Test
    public void testCutList() {
        CacheTest cacheProxy = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("qwe"));
        List result = cacheProxy.getList(100);
        Assertions.assertEquals(50, result.size());
    }

    @Test
    public void testKeyGenerationWhenSpecifiedArguments() {
        CacheTest cacheProxy = (CacheTest) cacheProxyImpl.cache(new CacheTestImpl("qwe"));
        cacheProxy.getStringSize("sda", 1213124);
        cacheProxy.getStringSize("sda", 23351);
        Assertions.assertEquals(1, cacheProxy.getCount());
    }


}