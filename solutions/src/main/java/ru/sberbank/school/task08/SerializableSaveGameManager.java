package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class SerializableSaveGameManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    public SerializableSaveGameManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Filename should be provided");
        Objects.requireNonNull(gameState, "No MapState provided");

        String fullFileName = filesDirectory + File.separator + filename;

        try (OutputStream os = new FileOutputStream(fullFileName);
                 ObjectOutputStream oos = new ObjectOutputStream(os)) {

            oos.writeObject(gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Save file not found", e, SaveGameException.Type.IO, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Some IO exception", e, SaveGameException.Type.IO, gameState);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Filename should be provided");

        String fullFileName = filesDirectory + File.separator + filename;

        try (InputStream is = new FileInputStream(fullFileName);
                 ObjectInputStream ois = new ObjectInputStream(is)) {

            return (MapState<GameObject>) ois.readObject();

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Save file not found", e, SaveGameException.Type.IO, null);
        } catch (IOException e) {
            throw new SaveGameException("Some IO exception", e, SaveGameException.Type.IO, null);
        } catch (ClassNotFoundException e) {
            throw new SaveGameException("Class not found", e, SaveGameException.Type.SYSTEM, null);
        }
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type, InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
