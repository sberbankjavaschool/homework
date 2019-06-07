package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task08.KryoSerializerMapState;
import ru.sberbank.school.task08.SaveGameException;
import ru.sberbank.school.task08.SaveGameManager;
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

    public KryoSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.register(MapState.class, new KryoSerializerMapState());
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);
    }

    @Override
    public void saveGame(String filename, Savable gameState) throws SaveGameException {
        try(OutputStream out = new FileOutputStream(filesDirectory + filename);
            Output output = new Output(out)){
            kryo.writeClassAndObject(output, gameState);
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found", ex, SaveGameException.Type.USER, gameState);
        } catch (IOException ex) {
            throw new SaveGameException("IO error", ex, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public Savable loadGame(String filename) throws SaveGameException {
        Savable load = null;
        try(InputStream in = new FileInputStream(filesDirectory + filename);
            Input input = new Input(in)){
            load = (Savable) kryo.readClassAndObject(input);
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found", ex, SaveGameException.Type.USER, load);
        } catch (IOException ex) {
            throw new SaveGameException("IO error", ex, SaveGameException.Type.IO, load);
        }
        return load;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type, InstantiatableEntity.Status status, long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState<>(name, entities);
    }
}
