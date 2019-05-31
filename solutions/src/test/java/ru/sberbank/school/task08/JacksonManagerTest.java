package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

public class JacksonManagerTest extends ManagerTest{

    private static JacksonManager manager;
    private static MapState<GameObject> mapState;

    @BeforeAll
    static void init() {
        manager = new JacksonManager(PATH);
        manager.initialize();

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.BUILDING,
            InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.ENEMY,
            InstantiatableEntity.Status.SPAWNED, 300));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.ITEM,
            InstantiatableEntity.Status.KILLED, 20));
        gameObjects.add(manager.createEntity(InstantiatableEntity.Type.NPC,
            InstantiatableEntity.Status.DESPAWNED, 500));
        mapState = manager.createSavable("Test", gameObjects);
    }

    @Test
    void saveAndLoadGame() throws SaveGameException {
        manager.saveGame(FILENAME, mapState);
        MapState<GameObject> loadedMapState = manager.loadGame(FILENAME);

        Assertions.assertEquals(mapState, loadedMapState);
        Assertions.assertNotSame(mapState, loadedMapState);
    }

    @Test
    void saveEmptyGameObjects() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            manager.saveGame(FILENAME, null);
        });
    }
}
