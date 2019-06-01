package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class KryoManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    private Kryo kryo;

    /**
     * Класс-наследник должен иметь конструктор эквивалентный этому.
     *
     * @param filesDirectoryPath Путь до директории, в которой хранятся файлы сохранений
     */
    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.register(MapState.class, new KryoSerializer());

        kryo.register(ArrayList.class);
        kryo.register(GameObject.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(InstantiatableEntity.Type.class);
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        Objects.requireNonNull(gameState, "Сохраняемый объект не должен быть null");

        try (FileOutputStream fos = new FileOutputStream(filesDirectory + File.separator + filename);
                Output output = new Output(fos)) {

            kryo.writeObject(output, gameState);

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
                Input inputStream = new Input(fis)) {

            savable = kryo.readObject(inputStream, MapState.class);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Файл не найден", e, SaveGameException.Type.USER, savable);
        } catch (IOException e) {
            throw new SaveGameException("Возникла ошибка при чтении объекта из потока", e,
                    SaveGameException.Type.IO, savable);
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
