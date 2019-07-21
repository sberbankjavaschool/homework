package ru.sberbank.school.task08;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.Arrays;
import java.util.List;

public class JavaSerializationTest {

    private MapState<GameObject> state;
    private final String path = ".\\src\\main\\java\\ru\\sberbank\\school\\task08\\temp\\";

    @Before
    public void prepare() {
        List<GameObject> objects = Arrays.asList(
                new GameObject(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.KILLED, 100),
                new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.DESPAWNED, 12),
                new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.SPAWNED, 12),
                new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.SPAWNED, 12)
        );
        state = new MapState<>("state", objects);
    }


    @Test
    @DisplayName("BuiltIn serialization")
    public void javaBuiltIn() throws SaveGameException {
        SerializableManager manager = new SerializableManager(path);
        manager.saveGame("state.txt", state);
        MapState<GameObject> newState = manager.loadGame("state.txt");
        Assertions.assertEquals(state, newState);
    }

    @Test
    @DisplayName("Kryo serialization")
    public void kryo() throws SaveGameException {
        KryoSerializableManager manager = new KryoSerializableManager(path);
        manager.initialize();
        manager.saveGame("state.txt", state);
        MapState<GameObject> newState = manager.loadGame("state.txt");
        Assertions.assertEquals(state, newState);
    }

    @Test
    @DisplayName("Jackson serialization")
    public void jackson() throws SaveGameException {
        JacksonSerializableManager manager = new JacksonSerializableManager(path);
        manager.initialize();
        manager.saveGame("state.txt", state);
        MapState<GameObject> newState = manager.loadGame("state.txt");
        Assertions.assertEquals(state, newState);
    }
}
