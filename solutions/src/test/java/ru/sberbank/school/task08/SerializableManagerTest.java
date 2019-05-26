package ru.sberbank.school.task08;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

public class SerializableManagerTest {

    public static SerializableManager manager;
    public static MapState<GameObject> mapState;

    @BeforeEach
    public void init() {

        manager = new SerializableManager(".");
        manager.initialize();

        GameObject hospital = new GameObject(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 100);
        GameObject princess = new GameObject(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.SPAWNED, 50);
        GameObject axe = new GameObject(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.SPAWNED, 1);
        GameObject dragon = new GameObject(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.KILLED, 0);

        List<GameObject> objects = new ArrayList<>();
        objects.add(hospital);
        objects.add(princess);
        objects.add(axe);
        objects.add(dragon);

        mapState = new MapState<>("level 1", objects);
    }

    @Test
    public void testReadWrite() {

        MapState<GameObject> loadMapState = null;

        try {
            String filename = mapState.getName() + ".serializable";
            manager.saveGame(filename, mapState);
            loadMapState = manager.loadGame(filename);
        } catch (SaveGameException e) {
            System.out.println(e);
            assertTrue(false);
        }

        Assertions.assertEquals(mapState, loadMapState);

    }

}
