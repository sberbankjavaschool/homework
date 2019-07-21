package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Solution(8)
public class JacksonSerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private ObjectMapper mapper;

    public JacksonSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        mapper = new ObjectMapper();
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        try {
            mapper.writeValue(new File(filesDirectory + filename), gameState);
        } catch (IOException e) {
            throw new SaveGameException("Произошла ошибка при сохранении игры",
                    e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        MapState<GameObject> result = null;
        try {
            result = mapper.readValue(new File(filesDirectory
                    + filename), new TypeReference<MapState<GameObject>>() {});
        } catch (IOException e) {
            throw new SaveGameException("Произошла ошибка при загрузке игры",
                    e, SaveGameException.Type.IO, result);
        }
        return result;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List entities) {
        return new MapState<>(name, entities);
    }
}
