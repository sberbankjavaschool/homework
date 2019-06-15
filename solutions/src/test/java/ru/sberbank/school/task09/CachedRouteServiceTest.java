package ru.sberbank.school.task09;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static java.io.File.separator;

class CachedRouteServiceTest {

    private final String path = new File("src/test/resources/task09").getAbsolutePath();

    private final CachedRouteService cachedRouteService = new CachedRouteService(path);

    private String from;
    private String to;
    private String key;
    private File file;

    @Test
    void cacheRouteTest() throws RouteFetchException {
        from = "Saint-Petersburg";
        to = "San-Francisco";
        prepare();
        Assertions.assertDoesNotThrow(() -> cachedRouteService.getRoute(from, to));
        Assertions.assertTrue(file.exists());
        clean();
    }

    @Test
    void fetchRouteTest() throws RouteFetchException {
        from = "San-Francisco";
        to = "New-York";
        prepare();
        Route<City> route = cachedRouteService.getRoute(from, to);
        Route<City> cachedRoute = cachedRouteService.getRoute(from, to);

        Assertions.assertNotSame(route, cachedRoute);
        Assertions.assertEquals(route.toString(), cachedRoute.toString());
        Assertions.assertIterableEquals(route.getCities(), cachedRoute.getCities());
        clean();
    }

    @Test
    void unknownCityTest() {
        from = "Dubai";
        to = "Mexico";

        Assertions.assertThrows(UnknownCityException.class, () -> cachedRouteService.getRoute(from, to));
        Assertions.assertThrows(UnknownCityException.class, () -> cachedRouteService.getRoute(from, null));
    }

    private void prepare() {
        key = from + "_" + to;
        file = new File(path + separator + key);
    }

    private void clean() {
        if (file.exists()) {
            file.delete();
        }
    }
}