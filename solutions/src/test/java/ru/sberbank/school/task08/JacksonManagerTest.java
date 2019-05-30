package ru.sberbank.school.task08;

import org.junit.jupiter.api.*;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

class JacksonManagerTest {
    private static JacksonManager jacksonManager;
    private static MapState<GameObject> mapState;
    private static String directoryPath = "C:\\Users\\1357028\\Desktop\\Save";
    private String fileName = "Hospital.txt";
    private static List<GameObject> objects;

    @BeforeAll
    static void init() {
        jacksonManager = new JacksonManager(directoryPath);
        jacksonManager.initialize();

        objects = new ArrayList<>();
        objects.add(jacksonManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 10));
        objects.add(jacksonManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.KILLED, 500));
        objects.add(jacksonManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.SPAWNED, 0));
        objects.add(jacksonManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 0));

        mapState = jacksonManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на выброс NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> jacksonManager.saveGame(null, mapState));
        Assertions.assertThrows(NullPointerException.class, () -> jacksonManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Проверка на выброс SaveGameException при некорректном пути к файлам сохранений")
    void saveGameFileNotFound() {
        jacksonManager = new JacksonManager("");
        Assertions.assertThrows(SaveGameException.class, () -> jacksonManager.saveGame(fileName, mapState));
        jacksonManager = new JacksonManager(directoryPath);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неидентичность объектов")
    void equalsSaveLoadObjects() throws SaveGameException {
        jacksonManager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = jacksonManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при пустом списке")
    void emptyListObjects() throws SaveGameException {
        mapState = jacksonManager.createSavable("Пустой список", new ArrayList<>());
        jacksonManager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = jacksonManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = jacksonManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при null списке")
    void nullListObjects() throws SaveGameException {
        mapState = jacksonManager.createSavable("Пустой список", null);
        jacksonManager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = jacksonManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = jacksonManager.createSavable("Госпиталь", objects);
    }

}