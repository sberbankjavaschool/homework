package ru.sberbank.school.task08;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Assert;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import org.junit.Before;
import org.junit.Test;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;


import java.util.ArrayList;
import java.util.List;

public class JacksonSerializeableManagerTest {
    private MapState mapState;
    private JacksonSerializeableManager manager;
    private String path;
    private String fileName;

    public JacksonSerializeableManagerTest() {
        path = "./src/test/java/ru/sberbank/school/task08";
        fileName = "game.bin";
    }

    @Before
    public void initialize () {
        manager = new JacksonSerializeableManager(path);
        manager.initialize();
        List<GameObject> list = new ArrayList<>();
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.KILLED, 5000L));
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add((GameObject) manager.createEntity(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 5000L));
        mapState = manager.createSavable("Game", list);
    }

    @Test
    public void serializeAndDeserialize () throws SaveGameException {
        manager.saveGame(fileName, mapState);
        Savable loadMapState = manager.loadGame(fileName);
        Assert.assertNotNull(mapState);
        Assert.assertNotNull(loadMapState);
        Assert.assertNotSame(mapState, loadMapState);
        Assert.assertEquals(mapState, loadMapState);
    }

    @Test
    public void serializeAndDeserializeEmptyList () throws SaveGameException {
        mapState = manager.createSavable("Game", null);
        manager.saveGame(fileName, mapState);
        Savable loadMapState = manager.loadGame(fileName);
        Assert.assertNotNull(mapState);
        Assert.assertNotNull(loadMapState);
        Assert.assertNotSame(mapState, loadMapState);
        Assert.assertEquals(mapState, loadMapState);
    }
}
