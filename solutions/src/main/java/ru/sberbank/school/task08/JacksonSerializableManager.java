package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;
import ru.sberbank.school.util.Solution;

import java.io.*;

import java.util.List;

@Solution(8)
public class JacksonSerializableManager extends SaveGameManager {


    /**
     * Класс-наследник должен иметь конструктор эквивалентный этому.
     *
     * @param filesDirectoryPath Путь до директории, в которой хранятся файлы сохранений
     */
    public JacksonSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }


    @Override
    public void initialize() {

    }


    @Override
    public void saveGame(String filename, Savable gameState) {
        try (OutputStream outputStream = new FileOutputStream(filesDirectory + filename);
             Output output = new Output(outputStream)) {
            new ObjectMapper().writeValue(output, gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Savable loadGame(String filename) {

        MapState mapState = null;

        try (InputStream inputStream = new FileInputStream(filesDirectory + filename);
             Input input = new Input(inputStream)) {
            mapState = new ObjectMapper().readValue(input, MapState.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapState;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState<GameObject>(name, entities);
    }
}
