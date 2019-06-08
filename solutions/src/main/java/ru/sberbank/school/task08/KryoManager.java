package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
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

import static java.io.File.separator;
import static ru.sberbank.school.task08.SaveGameException.Type.IO;

/**
 * 28.05.2019 сериализация с помощью Kryo
 *
 * @author Gregory Melnikov
 */

@Solution(8)
public class KryoManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private Kryo kryo;

    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.register(MapState.class, new MapStateSerializer());
        kryo.register(GameObject.class, new GameObjectSerializer());
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(ArrayList.class);
        kryo.setDefaultSerializer(FieldSerializer.class);
        kryo.setReferences(true);
    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState) throws SaveGameException {
        String path = filesDirectory + separator + filename;
        try (Output output = new Output(new FileOutputStream(path))) {
            
            kryo.writeObjectOrNull(output, gameState, MapState.class);

        } catch (IOException | NullPointerException | KryoException e) {
            throw new SaveGameException("KryoManager Saving error", e, IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        String path = filesDirectory + separator + filename;
        try (Input input = new Input(new FileInputStream(path))) {

            @SuppressWarnings("unchecked")
            MapState<GameObject> saved = kryo.readObjectOrNull(input, MapState.class);
            return saved;

        } catch (IOException | NullPointerException | ClassCastException | KryoException e) {
            throw new SaveGameException("KryoManager Loading error", e, IO, null);
        }
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                   long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }

    private static class MapStateSerializer extends Serializer<MapState<GameObject>> {
        @Override
        public void write(Kryo kryo, Output output, MapState<GameObject> mapState) {
            output.writeString(mapState.getName());
            kryo.writeObjectOrNull(output, mapState.getGameObjects(), ArrayList.class);
        }

        @Override
        public MapState<GameObject> read(Kryo kryo, Input input, Class<? extends MapState<GameObject>> type) {
            String name = input.readString();

            MapState<GameObject> mapState = kryo.newInstance(type);
            kryo.reference(mapState);

            @SuppressWarnings("unchecked")
            List<GameObject> gameObjects = kryo.readObjectOrNull(input, ArrayList.class);

            return new MapState<>(name, gameObjects);
        }
    }

    private static class GameObjectSerializer extends Serializer<GameObject> {
        @Override
        public void write(Kryo kryo, Output output, GameObject gameObject) {
            output.writeLong(gameObject.getHitPoints());
            kryo.writeObject(output, gameObject.getType());
            kryo.writeObject(output, gameObject.getStatus());
        }

        @Override
        public GameObject read(Kryo kryo, Input input, Class<? extends GameObject> type) {
            long hitPoints = input.readLong();
            InstantiatableEntity.Type objectType = kryo.readObject(input, InstantiatableEntity.Type.class);
            InstantiatableEntity.Status objectStatus = kryo.readObject(input, InstantiatableEntity.Status.class);

            return new GameObject(objectType, objectStatus, hitPoints);
        }
    }
}