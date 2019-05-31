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
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState) throws SaveGameException {
        try {
            mapper.writeValue(new File(filesDirectory + File.separator + filename), gameState);
        } catch (IOException e) {
            throw new SaveGameException("неудачная попытка сохранения");
        }
    }

    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        try {
            return mapper.readValue(new File(filesDirectory
                    + File.separator + filename), new TypeReference<MapState<GameObject>>() {});
        } catch (IOException e) {
            throw new SaveGameException("неудачная попытка загрузки");
        }
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
