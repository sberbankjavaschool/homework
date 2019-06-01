package ru.sberbank.school.task08;

import org.junit.jupiter.api.*;
import ru.sberbank.school.task08.state.*;

import java.util.ArrayList;
import java.util.List;

class JaxbManagerTest {
    private static JaxbManager jaxbManager;
    private static JaxbMapState<GameObject> mapState;
    private static String directoryPath = "C:\\Users\\1357028\\Desktop\\Save";
    private String fileName = "Hospital.txt";
    private static List<GameObject> objects;

    @BeforeAll
    static void init() {
        jaxbManager = new JaxbManager(directoryPath);
        jaxbManager.initialize();

        objects = new ArrayList<>();
        objects.add(jaxbManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 10));
        objects.add(jaxbManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.KILLED, 500));
        objects.add(jaxbManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.SPAWNED, 0));
        objects.add(jaxbManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 0));

        mapState = jaxbManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на выброс NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> jaxbManager.saveGame(null, mapState));
        Assertions.assertThrows(NullPointerException.class, () -> jaxbManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Проверка на выброс SaveGameException при некорректном пути к файлам сохранений")
    void saveGameFileNotFound() {
        jaxbManager = new JaxbManager("");
        Assertions.assertThrows(SaveGameException.class, () -> jaxbManager.saveGame(fileName, mapState));
        jaxbManager = new JaxbManager(directoryPath);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неидентичность объектов")
    void equalsSaveLoadObjects() throws SaveGameException {
        jaxbManager.saveGame(fileName, mapState);
        JaxbMapState<GameObject> otherMapState = jaxbManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при пустом списке")
    void emptyListObjects() throws SaveGameException {
        mapState = jaxbManager.createSavable("Пустой список", new ArrayList<>());
        jaxbManager.saveGame(fileName, mapState);
        JaxbMapState<GameObject> otherMapState = jaxbManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = jaxbManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при null списке")
    void nullListObjects() throws SaveGameException {
        mapState = jaxbManager.createSavable("Пустой список", null);
        jaxbManager.saveGame(fileName, mapState);
        JaxbMapState<GameObject> otherMapState = jaxbManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = jaxbManager.createSavable("Госпиталь", objects);
    }
}