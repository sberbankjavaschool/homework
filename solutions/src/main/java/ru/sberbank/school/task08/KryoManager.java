package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mart
 * 02.06.2019
 **/
@Solution(8)
public class KryoManager  extends SaveGameManager<MapState<GameObject>, GameObject> {
    private Kryo kryo;

    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.register(String.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(MapState.class, new MapStateSerializer());
        kryo.register(GameObject.class, new GameObjectSerializer());
        kryo.setDefaultSerializer(FieldSerializer.class);
        kryo.setReferences(true);
    }

    @Override
    public void saveGame(@NonNull String filename,
                         @NonNull MapState<GameObject> gameState) throws SaveGameException {
        String path = getPath(filename);

        try (Output out = new Output(new FileOutputStream(path))) {
            kryo.writeObject(out, gameState);
        } catch (IOException | NullPointerException e) {
            throw new SaveGameException("KryoManager saving error",
                    e.getCause(), SaveGameException.Type.IO, gameState);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        String path = getPath(filename);

        try (Input in = new Input(new FileInputStream(path))) {
            return kryo.readObject(in, MapState.class);
        } catch (IOException | NullPointerException e) {
            throw new SaveGameException("KryoManager loading error",
                    e.getCause(), SaveGameException.Type.IO, null);
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

    private String getPath(String filename) {
        return filesDirectory + File.separator + filename;
    }
}
