package ru.sberbank.school.task08;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

class KryoManagerTest {
    private static KryoManager kryoManager;
    private static MapState<GameObject> mapStateGameObjects;
    private static String path = "C:\\Users\\Anastasia\\Desktop\\Java\\serialize";
    private String fileName = "KryoManager.bin";
    private static List<GameObject> gameObjects;

    @BeforeAll
    static void initialization() {
        kryoManager = new KryoManager(path);
        kryoManager.initialize();
        gameObjects = new ArrayList<>();
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 200));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 400));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.KILLED, 10));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 0));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.DESPAWNED, 500));
        mapStateGameObjects = kryoManager.createSavable("Some_name_kryo", gameObjects);
        System.out.println("Before serialization: " + mapStateGameObjects);
    }

    @Test
    @DisplayName("Incorrect filesDirectoryPath")
    void saveGameFileNotFound() {
        kryoManager = new KryoManager("");
        Assertions.assertThrows(SaveGameException.class, () -> kryoManager.saveGame(fileName, mapStateGameObjects));
        kryoManager = new KryoManager(path);
    }

    @Test
    @DisplayName("NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, ()
                -> kryoManager.saveGame(null, mapStateGameObjects));
        Assertions.assertThrows(NullPointerException.class, () -> kryoManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Equals check")
    void equalsSaveLoadObjects() throws SaveGameException {
        kryoManager.saveGame(fileName, mapStateGameObjects);
        MapState<GameObject> otherMapState = kryoManager.loadGame(fileName);
        Assertions.assertEquals(mapStateGameObjects, otherMapState);
    }

}