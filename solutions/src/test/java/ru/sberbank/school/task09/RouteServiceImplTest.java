package ru.sberbank.school.task09;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class RouteServiceImplTest {
    static String tempDir;


    @BeforeAll
    static void setUp() throws IOException {
        Path path = Files.createTempDirectory("task09temp");
        tempDir = path.toString();
    }

    @Test
    void workingTest() {
        CacheService service = new KryoCacheService(tempDir);
        RouteService<City, Route<City>> rs = new RouteServiceImpl(service);
        Route<City> route =  rs.getRoute("Moscow", "Saint-Petersburg");
        Route<City> routeLoaded =  rs.getRoute("Moscow", "Saint-Petersburg");
        Assertions.assertAll(
                () -> Assertions.assertEquals(route.getRouteName(), routeLoaded.getRouteName()),
                () -> Assertions.assertIterableEquals(route.getCities(), routeLoaded.getCities())
        );
    }

    @AfterAll
    static void tearDown() {
        File tempDirFile = new File(tempDir);
        for (File file: Objects.requireNonNull(tempDirFile.listFiles())) {
            file.delete();
        }
        tempDirFile.delete();
    }
}