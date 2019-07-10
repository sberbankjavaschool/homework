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
public class KryoSerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private Kryo kryo;

    public KryoSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.register(MapState.class, new MapStateSerializer());
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(InstantiatableEntity.Type.class);
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        try (OutputStream outputStream = new FileOutputStream(filename);
                    Output output = new Output(outputStream)) {
            kryo.writeClassAndObject(output, gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        MapState<GameObject> object = null;
        try (InputStream inputStream = new FileInputStream(filename);
                    Input input = new Input(inputStream)) {
            object = (MapState<GameObject>) kryo.readClassAndObject(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
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
