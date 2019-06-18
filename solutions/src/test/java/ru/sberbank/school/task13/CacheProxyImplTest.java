package ru.sberbank.school.task13;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class CacheProxyImplTest {
    static String tempDir;

    @BeforeAll
    static void setUp() throws IOException {
        Path path = Files.createTempDirectory("task13temp");
        tempDir = path.toString();
    }

    @AfterAll
    static void tearDown() {
        File tempDirFile = new File(tempDir);
        for (File file: Objects.requireNonNull(tempDirFile.listFiles())) {
            file.delete();
        }
        tempDirFile.delete();
    }

    @Test
    void firstTryTest() {
        CacheProxyImpl cacheProxy = new CacheProxyImpl(tempDir);
        TestClassInterface cachedTestClass = cacheProxy.cache(new TestClassForCaching());
        cachedTestClass.methodWithTwoArgs(2, 5);
        cachedTestClass.methodWithTwoArgs(4, 5);
        cachedTestClass.methodWithTwoArgs(2, 5);
    }

    @Test
    void fileCacheTest() {
        CacheProxyImpl cacheProxy = new CacheProxyImpl(tempDir, CacheType.FILE);
        TestClassInterface cachedTestClass = cacheProxy.cache(new TestClassForCaching());
        cachedTestClass.methodWithTwoArgs(2, 5);
        cachedTestClass.methodWithTwoArgs(4, 5);
        cachedTestClass.methodWithTwoArgs(2, 5);
    }

    @Test
    void testAnnotatedMethod() {
        CacheProxyImpl cacheProxy = new CacheProxyImpl(tempDir);
        TestClassInterface cachedTestClass = cacheProxy.cache(new TestClassForCaching());
        cachedTestClass.methodWithAnnotations("word", 5, true);
        cachedTestClass.methodWithAnnotations("word", 5, true);
        cachedTestClass.methodWithAnnotations("word", 5, false);
    }
}