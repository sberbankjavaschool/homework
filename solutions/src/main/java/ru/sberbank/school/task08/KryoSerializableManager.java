package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import org.objenesis.strategy.StdInstantiatorStrategy;
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
    private String separator = File.separator;

    public KryoSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        kryo.register(MapState.class, new KryoSerializer());
        kryo.register(GameObject.class);
        kryo.register(ArrayList.class);
        kryo.register(InstantiatableEntity.Type.class);
        kryo.register(InstantiatableEntity.Status.class);

        if (filesDirectory.equals("")) {
            separator = "";
        }
    }

    @Override
    public void saveGame(@NonNull String filename,
                         @NonNull Savable gameState) throws SaveGameException {
        try (Output output =
                     new Output(new FileOutputStream(filesDirectory + separator + filename))) {
            kryo.writeObject(output, gameState);
        } catch (FileNotFoundException e) {
            throw new SaveGameException("Открыть файйл не удалось",
                    SaveGameException.Type.IO, gameState);
        } catch (KryoException e) {
            throw new SaveGameException("Не удалось записать класс в файл.",
                    SaveGameException.Type.SYSTEM, gameState);
        }
    }

    @Override
    public Savable loadGame(@NonNull String filename) throws SaveGameException {
        MapState<GameObject> savable = null;
        try (Input input = new Input(new FileInputStream(filesDirectory + separator + filename))) {
            savable = kryo.readObject(input, MapState.class);
        } catch (FileNotFoundException e) {
            throw new SaveGameException("Файл не найден", SaveGameException.Type.IO, savable);
        } catch (KryoException e) {
            throw new SaveGameException("В поток записан некорректный файл",
                    SaveGameException.Type.USER, savable);
        }

        return savable;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState<>(name, entities);
    }
}
