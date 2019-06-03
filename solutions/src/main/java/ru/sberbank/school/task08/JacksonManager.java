package ru.sberbank.school.task08;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mart
 * 02.06.2019
 **/
@Solution(8)
public class JacksonManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    private ObjectMapper objectMapper;

    public JacksonManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void saveGame(@NonNull String filename,
                         @NonNull MapState<GameObject> gameState) throws SaveGameException {
        String path = getPath(filename);

        try (FileOutputStream out = new FileOutputStream(path)) {
            objectMapper.writeValue(out, gameState);
        } catch (IOException | NullPointerException e) {
            throw new SaveGameException("JacksonManager saving error",
                    e.getCause(), SaveGameException.Type.IO, gameState);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        String path = getPath(filename);

        try (FileInputStream in = new FileInputStream(path)) {
            return objectMapper.readValue(in, MapState.class);
        } catch (IOException | NullPointerException e) {
            throw new SaveGameException("JacksonManager loading error",
                    e.getCause(), SaveGameException.Type.IO, null);
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

    private String getPath(String filename) {
        return filesDirectory + File.separator + filename;
    }
}
