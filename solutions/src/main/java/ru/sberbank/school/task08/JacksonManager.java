package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;

@Solution(8)
public class JacksonManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private ObjectMapper mapper;

    public JacksonManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        mapper = new ObjectMapper();
    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState)
            throws SaveGameException {
        try {
            mapper.writeValue(new File(filesDirectory + File.separator + filename), gameState);
        } catch (FileNotFoundException ex) {
            throw new SaveGameException("Файл не найден", ex, SaveGameException.Type.USER, gameState);
        } catch (IOException ex) {
            throw new SaveGameException("Ошибка записи", ex, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        MapState<GameObject> gameState = null;

        try {
            gameState = mapper.readValue(new File(filesDirectory +  File.separator + filename),
                new TypeReference<MapState<GameObject>>(){});
        } catch (JsonParseException e) {
            throw new SaveGameException("Ошибка парсера Jakson", e, SaveGameException.Type.IO, gameState);
        } catch (JsonMappingException e) {
            throw new SaveGameException("Ошибка мапинга Jakson", e, SaveGameException.Type.IO, gameState);
        } catch (IOException ex) {
            throw new SaveGameException("Ошибка чтения", ex, SaveGameException.Type.IO, gameState);
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
