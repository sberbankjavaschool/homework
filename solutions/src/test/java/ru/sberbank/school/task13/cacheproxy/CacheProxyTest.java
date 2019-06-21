package ru.sberbank.school.task13.cacheproxy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.cacheproxy.testclasses.TestInterface;
import ru.sberbank.school.task13.cacheproxy.testclasses.TestInterfaceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CacheProxyTest {
    private static String pathStr;
    private static TestInterface testInterface;

    @BeforeAll
    static void initialize() throws IOException {
        Path path = Files.createTempDirectory("resources");
        pathStr = path.toString();

        CacheProxy proxy = new CacheProxy(pathStr);
        testInterface = proxy.cache(new TestInterfaceImpl());
    }

    @AfterAll
    static void clear() {
        new File(pathStr).deleteOnExit();
    }

    @Test
    void toFileOneArgTest() {
        String strA = testInterface.toFileOneArg("Test");
        String strB = testInterface.toFileOneArg("Test");

        Assertions.assertEquals(strA, strB);
    }

    @Test
    void toRamOneArgTest() {
        String strA = testInterface.toRamOneArg("Test");
        String strB = testInterface.toRamOneArg("Test");

        Assertions.assertEquals(strA, strB);
    }

    @Test
    void toFileFirstArgIdentTest() {
        String strA = testInterface.toFileFirstArgIdent("Test", 5);
        String strB = testInterface.toFileFirstArgIdent("Test", 5);

        Assertions.assertEquals(strA, strB);
    }

    @Test
    void toRamFirstArgIdentTest() {
        String strA = testInterface.toRamFirstArgIdent("Test", 5);
        String strB = testInterface.toRamFirstArgIdent("Test", 5);

        Assertions.assertEquals(strA, strB);
    }

    @Test
    void toFileSecondArgIdentTest() {
        String strA = testInterface.toFileSecondArgIdent("Test", 5);
        String strB = testInterface.toFileFirstArgIdent("Test", 5);

        Assertions.assertEquals(strA, strB);
    }

    @Test
    void toRamSecondArgIdentTest() {
        String strA = testInterface.toRamSecondArgIdent("Test", 5);
        String strB = testInterface.toRamSecondArgIdent("Test", 5);

        Assertions.assertEquals(strA, strB);
    }
}
