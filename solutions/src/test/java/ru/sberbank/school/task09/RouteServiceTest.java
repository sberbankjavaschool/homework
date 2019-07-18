package ru.sberbank.school.task09;

import org.junit.jupiter.api.*;

import java.io.File;

public class RouteServiceTest {
    private final static String filePath =
            new File("src\\test\\java\\ru\\sberbank\\school\\task09").getAbsolutePath();

    private final static String cityOne = "London";
    private final static String cityTwo = "Moscow";

    private static RouteServiceExt routeService = new RouteServiceExt(filePath);

    @Test
    public void routeComparisonTest() {
        Route<City> route = routeService.getRoute(cityOne, cityTwo);
        Route<City> otherRoute = routeService.getRoute(cityOne, cityTwo);
        Route<City> falseRoute = routeService.getRoute(cityTwo, cityOne);

        Assertions.assertEquals(route.getRouteName(), otherRoute.getRouteName());
        Assertions.assertIterableEquals(route.getCities(), otherRoute.getCities());

        Assertions.assertNotEquals(otherRoute.getRouteName(), falseRoute.getRouteName());
        Assertions.assertNotEquals(otherRoute.getCities().get(0), falseRoute.getCities().get(0));
    }

    @Test
    public void existCityExceptionTest() {
        Assertions.assertDoesNotThrow(() -> routeService.getRoute(cityOne, cityTwo));
    }

    @Test
    public void unknownCityExceptionTest() {
        Assertions.assertThrows(UnknownCityException.class, () -> routeService.getRoute("Black Mesa", "Aperture"));
    }

    @AfterAll
    public static void clearAfterTest() {
        File file = new File(filePath + File.separator + cityOne + "_" + cityTwo);
        if (file.exists()) {
            file.delete();
        }

        File file2 = new File(filePath + File.separator + cityTwo + "_" + cityOne);
        if (file2.exists()) {
            file2.delete();
        }
    }
}