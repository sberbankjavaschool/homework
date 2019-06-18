package ru.sberbank.school.task13.cacheproxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CacheProxyTest {
    private ServiceTestInterface serviceProxy;
    private String rootPath;

    @Before
    public void init() throws IOException {
        Path path = Files.createTempDirectory("");
        rootPath = path.toString();

        CacheProxy proxy = new CacheProxy(rootPath);
        ServiceTest service = new ServiceTest();
        serviceProxy = (ServiceTestInterface) proxy.cache(service);
    }

    @Test
    public void testCacheInJvm() {
        String str1 = serviceProxy.cacheInJvm("Argument");
        String str2 = serviceProxy.cacheInJvm("Argument");
        Assertions.assertEquals(str1, str2);
    }

    @Test
    public void testCacheInFile() {
        Integer number1 = serviceProxy.cacheInFile(1, "Argument");
        Integer number2 = serviceProxy.cacheInFile(1, "Argument");
        Assertions.assertEquals(number1, number2);
    }

    @Test
    public void testCacheInJvmWithPropertyIdentityBy() {
        int number1 = serviceProxy.sumWithPropertyIdentityByFirstArgument(2, 4);
        int number2 = serviceProxy.sumWithPropertyIdentityByFirstArgument(2, 550);
        Assertions.assertEquals(number1, number2);
    }

    @Test
    public void testCacheInFileWithPropertyIdentityBy() {
        String str1 = serviceProxy.sumStringWithPropertyIdentityByFirstArgument(2, "cat", "dog");
        String str2 = serviceProxy.sumStringWithPropertyIdentityByFirstArgument(2, "ghghg", "ewww");
        Assertions.assertEquals(str1, str2);
    }

    @After
    public void end() {
        new File(rootPath).deleteOnExit();

    }

}
