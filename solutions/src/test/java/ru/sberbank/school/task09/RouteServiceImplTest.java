package ru.sberbank.school.task09;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

class RouteServiceImplTest {

    @Test
    void workingTest() {
        CacheService service = new KryoCacheService("f:/temp");
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

        for (File file: Objects.requireNonNull(new File("f:/temp").listFiles())) {
            file.delete();
        }
    }
}