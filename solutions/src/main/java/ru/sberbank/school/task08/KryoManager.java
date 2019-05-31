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

@Solution(8)
public class KryoManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private Kryo kryo = new Kryo();

    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }


    /**
     * Инициализацию класса можете осуществить в этом методе.
     * Он гарантированно будет вызван перед вызовом других методов класса.
     */
    @Override
    public void initialize() {
        kryo.register(MapState.class, new MapStateSerializer());
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);
    }

    /**
     * Сохраняет в файл.
     *
     * @param filename  имя файла
     * @param gameState состояние, которое нужно сохранить
     */
    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState) throws SaveGameException {
        try (OutputStream outputStream = new FileOutputStream(filesDirectory + File.separator + filename);
                 Output output = new Output(outputStream)) {
            kryo.writeObject(output, gameState);
        } catch (IOException e) {
            throw new SaveGameException("неудачная попытка сохранения");
        }
    }

    /**
     * Читает из файла.
     *
     * @param filename имя файла
     * @return загруженное состояние
     */
    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        try (FileInputStream fis = new FileInputStream(filesDirectory + File.separator + filename);
                 Input in = new Input(fis)) {
            return (MapState<GameObject>) kryo.readObject(in, MapState.class);
        } catch (IOException ex) {
            throw new SaveGameException("неудачная попытка загрузки");
        }
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
