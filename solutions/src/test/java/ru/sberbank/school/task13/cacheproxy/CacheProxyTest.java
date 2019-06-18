package ru.sberbank.school.task13.cacheproxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.beanfieldcopier.utils.Player;
import ru.sberbank.school.task13.cacheproxy.utils.TestClass;
import ru.sberbank.school.task13.cacheproxy.utils.TestClassImpl;

import java.io.File;

class CacheProxyTest {
    String pathRoot = "C:\\Users\\Anastasia\\Desktop\\Java\\serialize";
    CacheProxy cacheProxy = new CacheProxy(pathRoot);

    @Test
    public void testFromMemoryWithoutArgs() {
        TestClass proxyService = (TestClass) cacheProxy.cache(new TestClassImpl("test"));
        String res1 = proxyService.getResFromMemory();
        String res2 = proxyService.getResFromMemory();
        Assertions.assertEquals(res1, res2);
        Assertions.assertEquals(1, proxyService.getCountCalls());
    }

    @Test
    public void testFromMemoryWithArgs() {
        TestClass proxyService = (TestClass) cacheProxy.cache(new TestClassImpl("test"));
        String res1 = proxyService.getResFromMemoryWithArgs("test", 1);
        String res2 = proxyService.getResFromMemoryWithArgs("test", 1);
        Assertions.assertEquals(res1, res2);
        Assertions.assertEquals(1, proxyService.getCountCalls());
    }


    @Test
    public void testFromFileWithoutArgs() {
        TestClass proxyService = (TestClass) cacheProxy.cache(new TestClassImpl("test"));
        String res1 = proxyService.getResFromFile();
        String res2 = proxyService.getResFromFile();
        Assertions.assertEquals(res1, res2);
        Assertions.assertEquals(1, proxyService.getCountCalls());
    }

    @Test
    public void testFromFileWithArgs() {
        TestClass proxyService = (TestClass) cacheProxy.cache(new TestClassImpl("test"));
        String res3 = proxyService.getResFromFileWithArgs("test", 1);
        String res4 = proxyService.getResFromFileWithArgs("test", 1);
        Assertions.assertEquals(res3, res4);
        Assertions.assertEquals(1, proxyService.getCountCalls());
    }

    @Test
    public void testFromFileWithPlayers() {
        TestClass proxyService = (TestClass) cacheProxy.cache(new TestClassImpl("test"));
        Player player1 = new Player(14, "Ivanov", "SKA", "no");
        Player player2 = new Player(20, "Petrov", "SKA", "no");
        String res3 = proxyService.getResFromFileWithPlayers(player1, player2);
        String res4 = proxyService.getResFromFileWithPlayers(player1, player2);
        Assertions.assertEquals(res3, res4);
        Assertions.assertEquals(1, proxyService.getCountCalls());
    }

    @Test
    public void testFromZip() {
        TestClass proxyService = (TestClass) cacheProxy.cache(new TestClassImpl("test"));
        String res1 = proxyService.getResFromZip();
        String res2 = proxyService.getResFromZip();
        deleteAllFilesFolder(pathRoot);
        Assertions.assertEquals(res1, res2);
        Assertions.assertEquals(1, proxyService.getCountCalls());
    }

    public static void deleteAllFilesFolder(String path) {
        for (File myFile : new File(path).listFiles())
            if (myFile.isFile()) myFile.delete();
    }
}