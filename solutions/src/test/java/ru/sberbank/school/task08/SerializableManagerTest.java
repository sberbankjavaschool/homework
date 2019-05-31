package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

class SerializableManagerTest {
    private static SerializableManager serializableManager;
    private static MapState<GameObject> mapState;
    private static String directoryPath = System.getenv("directoryPath");
    private String fileName = "Hospital.txt";
    private static List<GameObject> objects;

    @BeforeAll
    static void init() {
        serializableManager = new SerializableManager(directoryPath);
        serializableManager.initialize();

        objects = new ArrayList<>();
        objects.add(serializableManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 10));
        objects.add(serializableManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.KILLED, 500));
        objects.add(serializableManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.SPAWNED, 0));
        objects.add(serializableManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 0));

        mapState = serializableManager.createSavable("Госпиталь", objects);
    }

    @Test
    @DisplayName("Проверка на выброс NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> serializableManager.saveGame(null, mapState));
        Assertions.assertThrows(NullPointerException.class, () -> serializableManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Проверка на выброс SaveGameException при некорректном пути к файлам сохранений")
    void saveGameFileNotFound() {
        serializableManager = new SerializableManager("");
        Assertions.assertThrows(SaveGameException.class, () -> serializableManager.saveGame(fileName, mapState));
        serializableManager = new SerializableManager(directoryPath);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неидентичность объектов")
    void equalsSaveLoadObjects() throws SaveGameException {
        serializableManager.saveGame(fileName, mapState);
        Savable otherMapState = serializableManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);
    }

    @Test
    @DisplayName("Проверка на эквивалентность и неиндетичность объектов при пустом списке")
    void emptyListObjects() throws SaveGameException {
        mapState = serializableManager.createSavable("Пустой список", new ArrayList<>());
        serializableManager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = serializableManager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
        Assertions.assertEquals(mapState, otherMapState);

        mapState = serializableManager.createSavable("Госпиталь", objects);
    }
}