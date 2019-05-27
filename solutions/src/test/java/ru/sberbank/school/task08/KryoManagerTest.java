package ru.sberbank.school.task08;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KryoManagerTest {
    private static KryoSerializableManager kryoSerializableManager;
    private static MapState<GameObject> mapState;
    private static String testFilesDirectoryPath =
            new File("src\\test\\java\\ru\\sberbank\\school\\task08\\files").getAbsolutePath();
    private static String incorrectFile = "incorrectFile";
    private static String fileName = "saveFile";

    @BeforeAll
    public static void init() {
        kryoSerializableManager = new KryoSerializableManager(testFilesDirectoryPath);
        kryoSerializableManager.initialize();
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add((GameObject) kryoSerializableManager
                .createEntity(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.KILLED, 100));
        mapState = (MapState) kryoSerializableManager.createSavable("Test map", gameObjects);
    }

    @Test
    public void loadInvalidFile() {
        Assertions.assertThrows(SaveGameException.class, () ->
                kryoSerializableManager.loadGame(incorrectFile));
    }

    @Test
    public void equalsSaveLoadStates() throws SaveGameException {
        kryoSerializableManager.saveGame(fileName, mapState);

        Savable deserializationState = kryoSerializableManager.loadGame(fileName);
        Assertions.assertEquals(mapState, deserializationState);
    }

    @Test
    public void gameObjectsIsNull() {
        MapState<GameObject> state = (MapState<GameObject>) kryoSerializableManager.createSavable("test", null);
        Assertions.assertDoesNotThrow(() -> kryoSerializableManager.saveGame(fileName, state));
    }
}
