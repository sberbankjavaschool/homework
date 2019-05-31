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

    private Kryo kryo;

    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();

        kryo.register(MapState.class, new KryoSerializer());
        kryo.register(ArrayList.class);
        kryo.register(GameObject.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(InstantiatableEntity.Type.class);
    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState)
            throws SaveGameException {

            try (FileOutputStream fos = new FileOutputStream(filesDirectory + File.separator + filename);
                 Output out = new Output(fos)) {
                kryo.writeObject(out, gameState);
            } catch (FileNotFoundException ex) {
                throw new SaveGameException("Файл не найден", ex, SaveGameException.Type.USER, gameState);
            } catch (IOException ex) {
                throw new SaveGameException("Ошибка записи", ex, SaveGameException.Type.IO, gameState);
            }
    }

    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        MapState<GameObject> gameState = null;

        try (FileInputStream fis = new FileInputStream(filesDirectory + File.separator + filename);
             Input in = new Input(fis)) {
            gameState = kryo.readObjectOrNull(in, MapState.class);
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("Файл не найден", ex, SaveGameException.Type.USER, gameState);
        } catch (IOException ex) {
            throw new SaveGameException("Ошибка чтения", ex, SaveGameException.Type.IO, gameState);
        }
        return gameState;
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
