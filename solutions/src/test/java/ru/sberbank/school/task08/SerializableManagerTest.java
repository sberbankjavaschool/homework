package ru.sberbank.school.task08;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

public class SerializableManagerTest {
    private SerializableManager manager;
    private MapState<GameObject> mapState;
    private String fileName = "save.txt";

    @Before
    public void init() {
        manager = new SerializableManager("./src/test/java/ru/sberbank/school/task08");

        manager.initialize();

        GameObject entity1 = manager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.KILLED, 555L);
        GameObject entity2 = manager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 5L);
        GameObject entity3 = manager.createEntity(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.DESPAWNED, 3L);

        List<GameObject> list = new ArrayList<>();
        list.add(entity1);
        list.add(entity2);
        list.add(entity3);

        mapState = new MapState<>("Test 1", list);
    }

    @Test
    public void writeAndReadTest() {
        MapState<GameObject> result = null;

        try {
            manager.saveGame(fileName, mapState);
            result = manager.loadGame(fileName);

        } catch (SaveGameException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(result, mapState);
    }
}
