package ru.sberbank.school.task09;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteServiceCachedTest {
    private static RouteServiceCached routeServiceCached;
    private static String path = "C:\\Users\\Anastasia\\Desktop\\Java\\task09Ser";

    @BeforeAll
    public static void initialization() {
        routeServiceCached = new RouteServiceCached(path);
    }

    @Test
    @DisplayName("Test Route equals")
    public void equalsRoutesTest() {
        Route<City> route = routeServiceCached.getRoute("Chelyabinsk", "Moscow");
        Route<City> route_2 = routeServiceCached.getRoute("Chelyabinsk", "Moscow");
        Assertions.assertEquals(route.getRouteName(), route_2.getRouteName());
        Assertions.assertIterableEquals(route.getCities(), route_2.getCities());
    }

    @Test
    @DisplayName("Timecheck")
    void timeCheck() {
        long startTime = System.nanoTime();
        System.out.println("Start_time " + startTime);
        Route<City> route_2 = routeServiceCached.getRoute("Chelyabinsk", "Moscow");
        System.out.println("After serialization " + (System.nanoTime() - startTime));
        Route<City> route = routeServiceCached.getRoute("Chelyabinsk", "Moscow");
        System.out.println("After deserialization " + (System.nanoTime() - startTime));
    }


}