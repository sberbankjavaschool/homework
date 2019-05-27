package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class JacksonManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    ObjectMapper objectMapper;

    public JacksonManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не может быть null");
        Objects.requireNonNull(gameState, "Состояние не может быть null");

        try {

            objectMapper.writeValue(new File(filesDirectory + File.separator + filename), gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("Отсутсвует файл", e, SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Проблемы с записью в файл", e, SaveGameException.Type.USER, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не может быть null");
        MapState<GameObject> gameState = null;

        try {

            gameState = objectMapper.readValue(new File(filesDirectory
                            + File.separator + filename), new TypeReference<MapState<GameObject>>(){});

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
