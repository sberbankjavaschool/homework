package ru.sberbank.school.task08;

import org.junit.jupiter.api.*;
import ru.sberbank.school.task08.state.*;

import java.util.ArrayList;
import java.util.List;


class KryoManagerTest {
    private static KryoManager kryoManager;
    private static MapState<GameObject> mapState;
    private static String directoryPath = "C:\\Users\\1357028\\Desktop\\Save";
    private String fileName = "Hospital.txt";
    private static List<GameObject> objects;

    @BeforeAll
    static void init() {
        kryoManager = new KryoManager(directoryPath);
        kryoManager.initialize();

        objects = new ArrayList<>();
        objects.add(kryoManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 10));
        objects.add(kryoManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.KILLED, 500));
        objects.add(kryoManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.SPAWNED, 0));
        objects.add(kryoManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 0));

        mapState = kryoManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на выброс NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> kryoManager.saveGame(null, mapState));
        Assertions.assertThrows(NullPointerException.class, () -> kryoManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Проверка на выброс SaveGameException при некорректном пути к файлам сохранений")
    void saveGameFileNotFound() {
        kryoManager = new KryoManager("");
        Assertions.assertThrows(SaveGameException.class, () -> kryoManager.saveGame(fileName, mapState));
        kryoManager = new KryoManager(directoryPath);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неидентичность объектов")
    void equalsSaveLoadObjects() throws SaveGameException {
        kryoManager.saveGame(fileName, mapState);

        Savable otherMapState = kryoManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при пустом списке")
    void emptyListObjects() throws SaveGameException {
        mapState = kryoManager.createSavable("Пустой список", new ArrayList<>());
        kryoManager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = kryoManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = kryoManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при null списке")
    void nullListObjects() throws SaveGameException {
        mapState = kryoManager.createSavable("Пустой список", null);
        kryoManager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = kryoManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = kryoManager.createSavable("Госпиталь", objects);
    }
}