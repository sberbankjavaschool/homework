package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.NonNull;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

@Solution(8)
public class KryoManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private Kryo kryo;

    /**
     * Конструктор не меняйте.
     */
    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.register(MapState.class, new KryoSerializer());
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");
        Objects.requireNonNull(gameState, "Parameter gameState must be not null!");

        String fullName = filesDirectory + "/" + filename;
        try (Output output = new Output(new FileOutputStream(fullName))) {
            kryo.writeObject(output, gameState);
        } catch (FileNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.USER, gameState);
        }

    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");

        String fullName = filesDirectory + "/" + filename;
        try (Input input = new Input(new FileInputStream(fullName))) {
            return kryo.readObject(input, MapState.class);
        } catch (FileNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.USER, null);
        }

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
