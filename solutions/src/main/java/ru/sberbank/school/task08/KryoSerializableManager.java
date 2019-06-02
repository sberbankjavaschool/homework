package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task08.kryo.MapStateSerializer;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;
import ru.sberbank.school.util.Solution;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Solution(8)
public class KryoSerializableManager extends SaveGameManager {

    private Kryo kryo;

    /**
     * Класс-наследник должен иметь конструктор эквивалентный этому.
     *
     * @param filesDirectoryPath Путь до директории, в которой хранятся файлы сохранений
     */
    public KryoSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        this.kryo = new Kryo();
        kryo.register(MapState.class, new MapStateSerializer());
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(InstantiatableEntity.Type.class);
    }


    @Override
    public void saveGame(@NonNull String filename, @NonNull Savable gameState) throws SaveGameException {
        try (OutputStream outputStream = new FileOutputStream(filesDirectory + filename);
             Output output = new Output(outputStream)) {
                kryo.writeObject(output, gameState);
        } catch (IOException e) {
            throw new SaveGameException("I/O operation has been failed", SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public Savable loadGame(@NonNull String filename) throws SaveGameException {

        MapState mapState = null;

        try (InputStream inputStream = new FileInputStream(filesDirectory + filename);
             Input input = new Input(inputStream)) {
                mapState = kryo.readObject(input, MapState.class);
        } catch (IOException e) {
            throw new SaveGameException("I/O operation has been failed", SaveGameException.Type.IO, mapState);
        }
        return mapState;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState<GameObject>(name, entities);
    }
}
