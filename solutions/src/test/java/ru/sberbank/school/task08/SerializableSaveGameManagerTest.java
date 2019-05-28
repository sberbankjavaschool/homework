package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

class SerializableSaveGameManagerTest {

    private final SerializableSaveGameManager manager = new SerializableSaveGameManager("f:/temp");

    @Test
    void managerWithEmptyPath() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new SerializableSaveGameManager(null);
        });
    }

    @Test
    void saveGameWithEmptyGameObjects() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            manager.saveGame("test.save", null);
        });
    }

    @Test
    void saveGameWithEmptyFilename() {
        MapState<GameObject> mapState = manager.createSavable("Test", null);
        Assertions.assertThrows(NullPointerException.class, () -> {
            manager.saveGame(null, mapState);
        });
    }

    @Test
    void saveGameWithWrongFilename() {
        MapState<GameObject> mapState = manager.createSavable("Test", null);
        Assertions.assertThrows(SaveGameException.class, () -> {
            manager.saveGame(".", mapState);
        });
    }

    @Test
    void loadGameWithWrongFilename() {
        Assertions.assertThrows(SaveGameException.class, () -> {
            manager.loadGame(".");
        });
    }

    @Test
    void saveGameSavesGame() throws SaveGameException {
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 1));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.DESPAWNED, 2));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 3));
        MapState<GameObject> mapState = manager.createSavable("Test", gameObjects);

        manager.saveGame("test.save", mapState);
        MapState<GameObject> loadedMapState = manager.loadGame("test.save");

        Assertions.assertEquals(mapState, loadedMapState);
    }

    @Test
    void saveGameWithNoName() throws SaveGameException {
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 1));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.DESPAWNED, 2));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 3));
        MapState<GameObject> mapState = manager.createSavable(null, gameObjects);

        manager.saveGame("test.save", mapState);
        MapState<GameObject> loadedMapState = manager.loadGame("test.save");

        Assertions.assertEquals(mapState, loadedMapState);
    }

    @Test
    void saveGameWithNoGameObjects() throws SaveGameException {
        MapState<GameObject> mapState = manager.createSavable("Test", null);
        manager.saveGame("test.save", mapState);

        MapState<GameObject> loadedMapState = manager.loadGame("test.save");
        Assertions.assertEquals(mapState, loadedMapState);
    }
}