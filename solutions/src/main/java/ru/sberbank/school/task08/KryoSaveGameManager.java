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
public class KryoSaveGameManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private final Kryo kryo = new Kryo();

    public KryoSaveGameManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo.register(MapState.class, new KryoSerializer());
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(InstantiatableEntity.Type.class);
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> mapState) throws SaveGameException {
        Objects.requireNonNull(filename, "Filename should be provided");
        Objects.requireNonNull(mapState, "No MapState provided");

        String fullFileName = filesDirectory + File.separator + filename;

        try (OutputStream os = new FileOutputStream(fullFileName);
                Output output = new Output(os)) {

            kryo.writeObject(output, mapState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Save file not found", e, SaveGameException.Type.IO, mapState);
        } catch (IOException e) {
            throw new SaveGameException("Some IO exception", e, SaveGameException.Type.IO, mapState);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Filename should be provided");
        MapState<GameObject> mapState = null;

        String fullFileName = filesDirectory + File.separator + filename;

        try (InputStream is = new FileInputStream(fullFileName);
                Input input = new Input(is)) {

            mapState = (MapState<GameObject>) kryo.readObject(input, MapState.class);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Save file not found", e, SaveGameException.Type.IO, mapState);
        } catch (IOException e) {
            throw new SaveGameException("Some IO exception", e, SaveGameException.Type.IO, mapState);
        }

        return mapState;
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
