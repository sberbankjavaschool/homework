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
    public void initialize() {
//        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        Objects.requireNonNull(gameState, "Сохраняемый объект не должен быть null");

        try (FileOutputStream fos = new FileOutputStream(filesDirectory + File.separator + filename);
                ObjectOutputStream outputStream = new ObjectOutputStream(fos)) {

            outputStream.writeObject(gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Файл не найден", e, SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Возникла ошибка при записи объекта в поток", e,
                    SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        MapState<GameObject> savable = null;

        try (FileInputStream fis = new FileInputStream(filesDirectory + File.separator + filename);
                ObjectInputStream inputStream = new ObjectInputStream(fis)) {

            savable = (MapState<GameObject>) inputStream.readObject();

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Файл не найден", e, SaveGameException.Type.USER, savable);
        } catch (IOException e) {
            throw new SaveGameException("Возникла ошибка при чтении объекта из потока", e,
                    SaveGameException.Type.IO, savable);
        } catch (ClassNotFoundException e) {
            throw new SaveGameException("Десериализуемый класс не найден", e, SaveGameException.Type.SYSTEM, savable);
        }

        return savable;
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
