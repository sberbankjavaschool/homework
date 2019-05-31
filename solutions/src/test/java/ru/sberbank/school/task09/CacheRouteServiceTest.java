package ru.sberbank.school.task09;

import org.junit.jupiter.api.*;
import ru.sberbank.school.task09.util.KryoManager;

import java.io.File;


class CacheRouteServiceTest {
    private static CacheRouteService cacheRouteService;
    private static String directoryPath;

    @BeforeAll
    static void init() {
        directoryPath = "C:\\Users\\1357028\\Desktop\\Save";
        cacheRouteService = new CacheRouteService(directoryPath, new KryoManager(directoryPath));
    }

    @AfterEach
    void deleteFile () {
        File file = new File(directoryPath + File.separator + "Saint-Petersburg_New-York.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Тест на эквивалентность и неидентичность объектов")
    void equalsSaveLoadObjects() {
        Route<City> route = cacheRouteService.getRoute("Saint-Petersburg", "New-York");
        Route<City> otherRoute = cacheRouteService.getRoute("Saint-Petersburg", "New-York");

        Assertions.assertNotSame(route, otherRoute);
        Assertions.assertEquals(route.toString(), otherRoute.toString());
    }

    @Test
    @DisplayName("Тест на выброс NP при аргументах равных null")
    void throwNP() {
        Assertions.assertThrows(NullPointerException.class, () -> cacheRouteService.getRoute(null, "New-York"));
        Assertions.assertThrows(NullPointerException.class, () -> cacheRouteService.getRoute("New-York", null));
    }

    @Test
    @DisplayName("Тест на выброс UnknownCity при аргументах заданных пустой строкой")
    void throwUnknownCity() {
        Assertions.assertThrows(RouteFetchException.class, () -> cacheRouteService.getRoute("Saint-Petersburg", ""));
        Assertions.assertThrows(RouteFetchException.class, () -> cacheRouteService.getRoute("", "New-York"));
    }

    @Test
    @DisplayName("")
    void otherManager() {
        cacheRouteService = new CacheRouteService(directoryPath, new SerializeManager(directoryPath));

        Route<City> route = cacheRouteService.getRoute("Saint-Petersburg", "New-York");
        Route<City> otherRoute = cacheRouteService.getRoute("Saint-Petersburg", "New-York");

        Assertions.assertNotSame(route, otherRoute);
        Assertions.assertEquals(route.toString(), otherRoute.toString());

        cacheRouteService = new CacheRouteService(directoryPath, new KryoManager(directoryPath));
    }
}

