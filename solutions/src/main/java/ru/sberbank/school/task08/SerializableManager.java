package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;

@Solution(8)
public class SerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState) throws SaveGameException {
        try (FileOutputStream fos = new FileOutputStream(filesDirectory + filename);
                    ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(gameState);
        } catch (IOException e) {
            throw new SaveGameException("Произошла ошибка при сохранении игры",
                                        e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        MapState<GameObject> result = null;
        try (FileInputStream fis = new FileInputStream(filesDirectory + filename);
                    ObjectInputStream in = new ObjectInputStream(fis)) {
            result = (MapState<GameObject>) in.readObject();
        } catch (IOException e) {
            throw new SaveGameException("Произошла ошибка при загрузке игры",
                                        e, SaveGameException.Type.IO, result);
        } catch (ClassNotFoundException e) {
            throw new SaveGameException("Произошла ошибка при загрузке игры",
                    e, SaveGameException.Type.SYSTEM, result);
        }
        return result;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List entities) {
        return new MapState<>(name, entities);
    }

}
