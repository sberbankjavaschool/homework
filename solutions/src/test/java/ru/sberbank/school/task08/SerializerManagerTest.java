package ru.sberbank.school.task08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;
import sun.print.resources.serviceui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SerializerManagerTest {
    private static SerializableManager serializableManager;
    private static MapState<GameObject> mapState;
    private static String testFilesDirectoryPath =
            new File("src\\test\\java\\ru\\sberbank\\school\\task08\\files").getAbsolutePath();
    private static String incorrectFile = "incorrectFile";
    private static String fileName = "saveFile";

    @BeforeAll
    public static void init() {
        serializableManager = new SerializableManager(testFilesDirectoryPath);
        serializableManager.initialize();
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add((GameObject) serializableManager
                .createEntity(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.KILLED, 100));
        mapState = (MapState) serializableManager.createSavable("Test map", gameObjects);
    }

    @Test
    public void loadInvalidFile() {
        Assertions.assertThrows(SaveGameException.class, () ->
                serializableManager.loadGame(incorrectFile));
    }

    @Test
    public void equalsSaveLoadStates() throws SaveGameException {
        serializableManager.saveGame(fileName, mapState);

        Savable deserializationState = serializableManager.loadGame(fileName);
        Assertions.assertEquals(mapState, deserializationState);
    }

    @Test
    public void gameObjectsIsNull() {
        MapState<GameObject> state = (MapState<GameObject>) serializableManager.createSavable("test", null);
        Assertions.assertDoesNotThrow(() -> serializableManager.saveGame(fileName, state));
    }
}
