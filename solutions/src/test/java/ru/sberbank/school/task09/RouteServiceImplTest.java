package ru.sberbank.school.task09;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task09.kryo.KryoSerializeManager;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

class RouteServiceImplTest {

    private static RouteService service;
    private static String directory;
    private static Route<City> saved;
    private static Route<City> loaded;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init() {
        directory = "F:\\Task09Cache";
        service = new RouteServiceImpl(directory, new KryoSerializeManager(directory));
        saved = service.getRoute("Moscow", "Novosibirsk");
        loaded = service.getRoute("Moscow", "Novosibirsk");
    }

    @AfterAll
    static void cleanUp() {
        try {
            File fileDirectory = new File(directory);
            for (File file : fileDirectory.listFiles()) {
                file.delete();
            }
        } catch (NullPointerException e) {
            //noop
        }

    }

    @Test
    @DisplayName("save to cache test")
    void save() {
        File file = new File(directory + File.separator + "Moscow" + "_" + "Novosibirsk");
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("not same test")
    void notSame() {
        assertNotSame(saved, loaded);
    }

    @Test
    @DisplayName("not equals test")
    void notEquals() {
        assertNotEquals(saved, loaded);
    }

    @Test
    @DisplayName("identity test")
    void identity() {
        assertEquals(saved.toString(), loaded.toString());
        assertEquals(saved.getCities().toString(), loaded.getCities().toString());
    }

    @Test
    @DisplayName("NPE if null input CityName or IllegalArgs if empty test")
    void badInput() {
        assertThrows(NullPointerException.class, () -> service.getRoute(null, "Moscow"));
        assertThrows(IllegalArgumentException.class, () -> service.getRoute("", "Moscow"));
    }

    @Test
    @DisplayName("IllegalArgs Exception test with empty List<City> or NPE if null test")
    @SuppressWarnings("unchecked")
    void illegalArgs() {
        assertThrows(IllegalArgumentException.class, () -> service.createRoute(new ArrayList()));
        assertThrows(NullPointerException.class, () -> service.createRoute(null));
    }

    @Test
    @DisplayName("NPE if null in constructor input test")
    void constructorArgsNpe() {
        assertThrows(NullPointerException.class,() -> new RouteServiceImpl(null));
    }

    @Test
    @DisplayName("IllegalArgsEx in constructor with empty line input test")
    void constructorArgsIllegal() {
        assertThrows(IllegalArgumentException.class, () -> new RouteServiceImpl(""));
    }
}