package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JaxbManagerTest {

    public static JaxbManager manager;
    public static MapState<GameObject> mapState;
    String filename;

    @BeforeEach
    public void init() {

        manager = new JaxbManager(".");
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
//        objects.add(hospital);
//        objects.add(princess);
//        objects.add(axe);
//        objects.add(dragon);
        mapState = new MapState<>("", objects);
        filename = mapState.getName() + ".jaxb";

    }

    @Test
    public void testSaveLoad() {

        MapState<GameObject> loadMapState = null;

        try {
            manager.saveGame(filename, mapState);
            loadMapState = manager.loadGame(filename);
        } catch (SaveGameException e) {
            System.out.println(e);
            assertTrue(false);
        }

        Assertions.assertEquals(mapState, loadMapState);

    }

    @Test
    void testSaveNull() {
        Assertions.assertThrows(NullPointerException.class, () -> manager.saveGame(null, mapState));
        Assertions.assertThrows(NullPointerException.class, () -> manager.saveGame(filename, null));
    }

    @Test
    void testNull() throws SaveGameException {
        mapState = manager.createSavable("Пустое состояние", null);
        manager.saveGame(filename, mapState);
        MapState<GameObject> loadMapState = manager.loadGame(filename);

        Assertions.assertEquals(mapState, loadMapState);
        Assertions.assertNotSame(mapState, loadMapState);
    }

}
