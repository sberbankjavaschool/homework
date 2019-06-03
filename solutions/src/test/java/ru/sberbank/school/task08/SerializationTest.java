package ru.sberbank.school.task08;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Mart
 * 03.06.2019
 **/
public class SerializationTest {
    private String fileName = "saved.tmp";
    private MapState<GameObject> saved;
    private String directoryPath = "D:\\";

    @BeforeEach
    void init() {
        List<GameObject> list = new ArrayList<>();
        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.DESPAWNED, 500));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 0));
        saved = new MapState<>("mapState", list);
    }

    @Test
    void serializableManagerTest() {
        try {
            SerializableManager manager = new SerializableManager(directoryPath);
            manager.saveGame(fileName, saved);
            MapState<GameObject> loaded = manager.loadGame(fileName);
            assertEquals(saved, loaded);
            System.out.println("====serializable====");
            System.out.println(saved);
            System.out.println(loaded);
            System.out.println("============");
        } catch (SaveGameException e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }

    @Test
    void kryoManagerTest() {
        try {
            KryoManager manager = new KryoManager(directoryPath);
            manager.initialize();
            manager.saveGame(fileName, saved);
            MapState<GameObject> loaded = manager.loadGame(fileName);
            assertEquals(saved, loaded);
            System.out.println("====kryo====");
            System.out.println(saved);
            System.out.println(loaded);
            System.out.println("============");
        } catch (SaveGameException e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }

    @Test
    void jacksonManagerTest() {
        try {
            JacksonManager manager = new JacksonManager(directoryPath);
            manager.initialize();
            manager.saveGame(fileName, saved);
            MapState<GameObject> loaded = manager.loadGame(fileName);
            assertEquals(saved, loaded);
            System.out.println("====jackson====");
            System.out.println(saved);
            System.out.println(loaded);
            System.out.println("============");
        } catch (SaveGameException e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }
}
