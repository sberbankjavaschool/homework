package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JaksonManagerTest {
    private static JacksonSerializableManager jacksonSerializableManager;
    private static MapState<GameObject> mapState;
    private static String testFilesDirectoryPath =
            new File("src\\test\\java\\ru\\sberbank\\school\\task08\\files").getAbsolutePath();
    private static String incorrectFile = "incorrectFile";
    private static String fileName = "saveFile";

    @BeforeAll
    public static void init() {
        jacksonSerializableManager = new JacksonSerializableManager(testFilesDirectoryPath);
        jacksonSerializableManager.initialize();
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add((GameObject) jacksonSerializableManager
                .createEntity(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.KILLED, 100));
        mapState = (MapState) jacksonSerializableManager.createSavable("Test map", gameObjects);
    }

    @Test
    public void loadInvalidFile() {
        Assertions.assertThrows(SaveGameException.class, () ->
                jacksonSerializableManager.loadGame(incorrectFile));
    }

    @Test
    public void equalsSaveLoadStates() throws SaveGameException {
        jacksonSerializableManager.saveGame(fileName, mapState);

        Savable deserializationState = jacksonSerializableManager.loadGame(fileName);
        Assertions.assertEquals(mapState, deserializationState);
    }

    @Test
    public void gameObjectsIsNull() {
        MapState<GameObject> state = (MapState<GameObject>) jacksonSerializableManager.createSavable("test", null);
        Assertions.assertDoesNotThrow(() -> jacksonSerializableManager.saveGame(fileName, state));
    }
}
