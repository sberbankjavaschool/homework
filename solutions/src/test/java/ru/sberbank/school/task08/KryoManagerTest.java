package ru.sberbank.school.task08;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

public class KryoManagerTest {
    private static KryoManager manager;
    private static MapState<GameObject> mapState;
    private static String filePath = "./src/test/java/ru/sberbank/school/task08";
    private static String fileName = "kryo";

    @Before
    public void init() {
        manager = new KryoManager(filePath);
        manager.initialize();
        List<GameObject> gameObjectList = new ArrayList<>();
        gameObjectList.add((GameObject) manager
                .createEntity(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 0));
        gameObjectList.add((GameObject) manager
                .createEntity(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.SPAWNED, 50));
        gameObjectList.add((GameObject) manager
                .createEntity(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.KILLED, 0));
        mapState = manager.createSavable("Test state", gameObjectList);
    }

    @Test
    @DisplayName("Equals test")
    public void equalsTest() throws SaveGameException {
        manager.saveGame(fileName, mapState);
        Savable loadedObject = manager.loadGame(fileName);
        Assertions.assertEquals(mapState, loadedObject);
    }
}

