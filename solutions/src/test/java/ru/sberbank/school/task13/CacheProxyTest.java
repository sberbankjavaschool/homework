package ru.sberbank.school.task13;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.annotations.Cache;

import java.io.File;

import static ru.sberbank.school.task13.enums.CacheType.FILE;
import static ru.sberbank.school.task13.enums.CacheType.JVM;

class CacheProxyTest {

    private final String path = new File("src/test/resources/task13").getAbsolutePath();

    private final CacheProxy proxy = new CacheProxy(path);

    private Plant plantProxy;
    private Tree plantService = new Tree();

    {
        plantService.setAge(76);
        plantService.setHeight(356);
        plantService.setEvergreen(true);
        plantService.setClimateZone("any");
        plantProxy = proxy.cache(plantService);
    }

    @Test
    void jvmCacheTest() {
        Plant restoredPlant1 = plantProxy.toMemory();
        Plant restoredPlant2 = plantProxy.toMemory();
        Assertions.assertSame(restoredPlant1, restoredPlant2);
    }

    @Test
    void fileCacheTest() {
        Integer restoredInteger1 = plantProxy.restoreAge(21);
        Integer restoredInteger2 = plantProxy.restoreAge(21);
        Assertions.assertEquals(restoredInteger1, restoredInteger2);
    }

    interface Plant {

        @Cache(cacheType = FILE, fileNamePrefix = "PlantTest_")
        Integer restoreAge(int height);

        @Cache(cacheType = JVM)
        Plant toMemory();
    }

    @Data
    class Tree implements Plant {
        private boolean evergreen;
        private Integer age;
        private long height;
        private String climateZone;

        @Override
        public Tree toMemory() {
            return this;
        }

        @Override
        public Integer restoreAge(int height) {
            setHeight(height);
            return age + height;
        }
    }
}
