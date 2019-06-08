package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.MapState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.school.task08.state.InstantiatableEntity.Status.*;
import static ru.sberbank.school.task08.state.InstantiatableEntity.Type.*;

class KryoManagerTest {

    private MapState<GameObject> mapState;
    private List<GameObject> gameObjects = new ArrayList<>();
    private String fileName = "KryoFile.bin";
    private String filesDirectoryPath = new File("src/test/resources/task08").getAbsolutePath();
    private KryoManager manager = new KryoManager(filesDirectoryPath);

    {
        gameObjects.add(manager.createEntity(NPC, SPAWNED, 50));
        gameObjects.add(manager.createEntity(NPC, DESPAWNED, 0));
        gameObjects.add(manager.createEntity(ENEMY, SPAWNED, 100));
        gameObjects.add(manager.createEntity(ENEMY, KILLED, 0));
        gameObjects.add(manager.createEntity(ITEM, SPAWNED, 10));
        gameObjects.add(manager.createEntity(BUILDING, SPAWNED, 1000));

        mapState = manager.createSavable("level33", gameObjects);
        manager.initialize();
    }

    @Test
    void saveGamePositiveTest() throws SaveGameException {
        manager.saveGame(fileName, mapState);
    }

    @Test
    void loadGamePositiveTest() throws SaveGameException {
        manager.saveGame(fileName, mapState);
        manager.loadGame(fileName);
    }

    @Test
    void loadGameFileNotExistTest() throws SaveGameException {
        String notExistFile = "NotExistFile.json";
        Assertions.assertThrows(SaveGameException.class, () -> manager.loadGame(notExistFile));
    }

    @Test
    void saveLoadGameEqualsTest() throws SaveGameException {
        manager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = manager.loadGame(fileName);

        Assertions.assertEquals(mapState, otherMapState);
        System.out.println(mapState.toString());
        System.out.println(otherMapState.toString());
    }

    @Test
    void saveLoadGameNotSameTest() throws SaveGameException {
        manager.saveGame(fileName, mapState);
        MapState<GameObject> otherMapState = manager.loadGame(fileName);

        Assertions.assertNotSame(mapState, otherMapState);
    }

    @Test
    void saveLoadGameNullObjectsTest() throws SaveGameException {
        MapState localMapState = manager.createSavable("level13", null);

        manager.saveGame(fileName, localMapState);
        MapState<GameObject> otherMapState = manager.loadGame(fileName);

        Assertions.assertEquals(localMapState, otherMapState);
        Assertions.assertNotSame(localMapState, otherMapState);
        System.out.println(localMapState.toString());
        System.out.println(otherMapState.toString());
    }
}