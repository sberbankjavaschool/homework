package ru.sberbank.school.task09;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;


public class KryoRouteServiceTest {
    private static KryoRouteService routeService;
    private static String path = "/Users/nick/Documents/homework-new/solutions/src/test/resources";

    @BeforeAll
    static void init() {
        routeService = new KryoRouteService(path);
    }

    @Test
    public void checkCachedRoutes() {
        Route<City> original = routeService.getRoute("Saint-Petersburg", "Vladivostok");
        Route<City> fromCache = routeService.getRoute("Saint-Petersburg", "Vladivostok");

        Assertions.assertEquals(original.getRouteName(), fromCache.getRouteName());
        Assertions.assertEquals(original.getCities(), fromCache.getCities());
        Assertions.assertNotSame(original, fromCache);
    }
}
