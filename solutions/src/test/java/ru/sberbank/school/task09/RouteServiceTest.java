package ru.sberbank.school.task09;

import org.junit.jupiter.api.*;

import java.io.File;

public class RouteServiceTest {
    private static String testFilesDirectoryPath =
            new File("src\\test\\java\\ru\\sberbank\\school\\task09").getAbsolutePath();

    private static RouteServiceImpl routeService = new RouteServiceImpl(testFilesDirectoryPath);

    @Test
    @DisplayName("Сравнение сгенерированного и считанного из кэша объектов.")
    public void routeComparisonTest() {
        Route<City> route = routeService.getRoute("Moscow", "Berlin");
        Route<City> otherRoute = routeService.getRoute("Moscow", "Berlin");
        Assertions.assertEquals(route.getRouteName(), otherRoute.getRouteName());
        Assertions.assertIterableEquals(route.getCities(), otherRoute.getCities());
    }

    @Test
    public void unknownCityExceptionTest() {
        Assertions.assertThrows(UnknownCityException.class, () -> routeService.getRoute("Troy", "Babylon"));
    }

    @AfterAll
    public static void deleteFiles() {
        File file = new File(testFilesDirectoryPath + File.separator + "Moscow_Berlin");
        if (file.exists()) {
            file.delete();
        }
    }
}
