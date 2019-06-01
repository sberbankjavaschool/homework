package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class SerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    /**
     * Конструктор не меняйте.
     */
    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() { }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не может быть null");
        Objects.requireNonNull(gameState, "Состояние не может быть null");

        try (OutputStream os = new FileOutputStream(filesDirectory + File.separator + filename);
                                                   ObjectOutputStream oos = new ObjectOutputStream(os)) {

            oos.writeObject(gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Отсутсвует файл", e, SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Ошибка записи в файл", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не может быть null");
        MapState<GameObject> gameState = null;

        try (InputStream is = new FileInputStream(filesDirectory + File.separator + filename);
                                                  ObjectInputStream ois = new ObjectInputStream(is)) {

            gameState = (MapState<GameObject>) ois.readObject();

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Отсутсвует файл", e, SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Ошибка при чтении из файла", e, SaveGameException.Type.IO, gameState);
        } catch (ClassNotFoundException e) {
            throw new SaveGameException("Не найти класс", e, SaveGameException.Type.IO, gameState);
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
