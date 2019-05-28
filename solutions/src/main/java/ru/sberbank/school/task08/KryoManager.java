package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

@Solution(8)
public class KryoManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    private Kryo kryo;

    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        Kryo kryo = new Kryo();
        KryoSerializer kryoSerializer = new KryoSerializer();
        kryo.register(MapState.class, kryoSerializer);
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState) throws SaveGameException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             Output out = new Output(fos)) {
            kryo.writeObject(out, gameState);
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        try (FileInputStream fis = new FileInputStream(filename);
             Input in = new Input(fis)) {
            return (MapState<GameObject>) kryo.readObject(in, MapState.class);
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
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
