package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;

@Solution(8)
public class SerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    /**
     * Конструктор не меняйте.
     */
    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState)
        throws SaveGameException {
        try (FileOutputStream fos = new FileOutputStream(filesDirectory + File.separator + filename);
                ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(gameState);
        } catch (IOException ex) {
            throw new SaveGameException("Write error", ex, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        MapState<GameObject> gameState = null;

        try (FileInputStream fis = new FileInputStream(filesDirectory + File.separator + filename);
                ObjectInputStream in = new ObjectInputStream(fis)) {
            gameState = (MapState<GameObject>) in.readObject();
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found", ex, SaveGameException.Type.USER, gameState);
        } catch (IOException ex) {
            throw new SaveGameException("Read error", ex, SaveGameException.Type.IO, gameState);
        } catch (ClassNotFoundException ex) {
            throw new SaveGameException("Class not found", ex, SaveGameException.Type.SYSTEM, gameState);
        }
        return gameState;
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                   InstantiatableEntity.Status status,
                                   long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }

}
