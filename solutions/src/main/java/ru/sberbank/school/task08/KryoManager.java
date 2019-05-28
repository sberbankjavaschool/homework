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
public class KryoManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private Kryo kryo;

    public KryoManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();

        kryo.register(MapState.class, new  SerializerForKryo());
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);

    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не может быть null");
        Objects.requireNonNull(gameState, "Состояние не может быть null");

        try (OutputStream os = new FileOutputStream(filesDirectory + File.separator + filename);
            Output output = new Output(os)) {

            kryo.writeObjectOrNull(output, gameState, MapState.class);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Отсутсвует файл", e, SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Ошибка записи в файл", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не может быть null");
        MapState<GameObject> gameState = null;

        try (InputStream is = new FileInputStream(filesDirectory + File.separator + filename)) {
            try (Input in = new Input(is)) {

                gameState = kryo.readObjectOrNull(in, MapState.class);

            }
        } catch (FileNotFoundException e) {
            throw new SaveGameException("Отсутсвует файл", e, SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Ошибка при чтении из файла", e, SaveGameException.Type.IO, gameState);
        }

        return gameState;
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status, long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
