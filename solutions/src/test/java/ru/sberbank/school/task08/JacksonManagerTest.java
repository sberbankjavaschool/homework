package ru.sberbank.school.task08;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JacksonManagerTest {
    private static JacksonManager jacksonManager;
    private static MapState<GameObject> mapStateGameObjects;
    private static String path = "C:\\Users\\Anastasia\\Desktop\\Java\\serialize";
    private String fileName = "JacksonManager.json";
    private static List<GameObject> gameObjects;

    @BeforeAll
    static void initialization() {
        jacksonManager = new JacksonManager(path);
        jacksonManager.initialize();
        gameObjects = new ArrayList<>();
        gameObjects.add(jacksonManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(jacksonManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 200));
        gameObjects.add(jacksonManager.createEntity(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.SPAWNED, 400));
        gameObjects.add(jacksonManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.KILLED, 10));
        gameObjects.add(jacksonManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 0));
        gameObjects.add(jacksonManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.DESPAWNED, 500));
        mapStateGameObjects = jacksonManager.createSavable("Some_name_json", gameObjects);
        System.out.println("Before serialization: " + mapStateGameObjects);
    }

    @Test
    @DisplayName("Incorrect filesDirectoryPath")
    void saveGameFileNotFound() {
        jacksonManager = new JacksonManager("");
        Assertions.assertThrows(SaveGameException.class, () -> jacksonManager.saveGame(fileName, mapStateGameObjects));
        jacksonManager = new JacksonManager(path);
    }

    @Test
    @DisplayName("NullPointerException")
    void saveGameNull() {
        Assertions.assertThrows(NullPointerException.class, ()
                -> jacksonManager.saveGame(null, mapStateGameObjects));
        Assertions.assertThrows(NullPointerException.class, () -> jacksonManager.saveGame(fileName, null));
    }

    @Test
    @DisplayName("Equals check")
    void equalsSaveLoadObjects() throws SaveGameException {
        jacksonManager.saveGame(fileName, mapStateGameObjects);
        MapState<GameObject> otherMapState = jacksonManager.loadGame(fileName);
        Assertions.assertEquals(mapStateGameObjects, otherMapState);
    }
}