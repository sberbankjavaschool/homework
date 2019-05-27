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
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             Output out = new Output(fos)) {
            kryo.writeObject(out, gameState);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        try (FileInputStream fis = new FileInputStream(filename);
             Input in = new Input(fis)) {
            return (MapState<GameObject>) kryo.readObject(in, MapState.class);
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

    public static void main(String[] args) {
        SerializableManager serializableManager
                = new SerializableManager("C:\\Users\\Anastasia\\Desktop\\Java\\serialize");
        serializableManager.initialize();
    }
}
