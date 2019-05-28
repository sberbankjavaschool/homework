package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SerializableManagerTest {
    private static SerializableManager serializableManager;
    private static MapState<GameObject> mapStateGameObjects;
    private static String path = "C:\\Users\\Anastasia\\Desktop\\Java\\serialize";
    private String fileName = "SerializableManager.bin";
    private static List<GameObject> gameObjects;

    @BeforeAll
    static void initialization() {
        serializableManager = new SerializableManager(path);
        serializableManager.initialize();
        gameObjects = new ArrayList<>();
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 200));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 400));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.KILLED, 10));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 0));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.DESPAWNED, 500));
        mapStateGameObjects = serializableManager.createSavable("Some_name_serializable", gameObjects);

    }

    @Test
    @DisplayName("Incorrect filesDirectoryPath")
    void saveGameFileNotFound() {
        serializableManager = new SerializableManager("");
        Assertions.assertThrows(SaveGameException.class, () -> serializableManager.saveGame(fileName, mapStateGameObjects));
        serializableManager = new SerializableManager(path);
    }

    @Test
    @DisplayName("NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, ()
                -> serializableManager.saveGame(null, mapStateGameObjects));
        Assertions.assertThrows(NullPointerException.class, () -> serializableManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Equals check")
    void equalsSaveLoadObjects() throws SaveGameException {
        serializableManager.saveGame(fileName, mapStateGameObjects);
        MapState<GameObject> otherMapState = serializableManager.loadGame(fileName);
        Assertions.assertEquals(mapStateGameObjects, otherMapState);
    }
}