package ru.sberbank.school.task08;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;


public class SerializableManagerTest {
    private static SerializableManager manager;
    private static String path = "/Users/nick/Documents/homework-new/solutions/src/test/resources";
    private static String fileName = "test.bin";
    private static MapState<GameObject> mapState;
    private static List<GameObject> gameObjects;

    @BeforeAll
    static void init() {
        manager = new SerializableManager(path);
        manager.initialize();

        gameObjects = new ArrayList<>();
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.KILLED, 0));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.SPAWNED, 10L));

        mapState = manager.createSavable("Test", gameObjects);
    }

    @Test
    public void checkClonedObject() throws SaveGameException {
        manager.saveGame(fileName, mapState);
        MapState<GameObject> clonedMap = manager.loadGame(fileName);

        Assertions.assertEquals(mapState, clonedMap);
        Assertions.assertNotSame(mapState, clonedMap);
    }
}
